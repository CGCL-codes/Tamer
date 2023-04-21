package ru.curs.showcase.test;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.*;

import ru.curs.showcase.app.api.*;
import ru.curs.showcase.app.api.datapanel.DataPanelElementInfo;
import ru.curs.showcase.app.api.event.*;
import ru.curs.showcase.model.chart.ChartGetCommand;
import ru.curs.showcase.model.command.ServerStateGetCommand;
import ru.curs.showcase.model.datapanel.DataPanelGetCommand;
import ru.curs.showcase.model.html.xform.*;
import ru.curs.showcase.runtime.*;
import ru.curs.showcase.util.xml.*;
import ru.curs.showcase.util.xml.XMLUtils;

/**
 * Тесты на получение информации о сессии пользователя.
 * 
 * @author den
 * 
 */
public class RuntimeTest extends AbstractTest {
	private static final String LEVEL_LOG_EVENT_PROP = "level";
	private static final boolean AUTH_VIA_AUTH_SERVER = true;
	private static final String TEMP_PASS = "pass";
	private static final String FAKE_SESSION_ID = "fake-session-id";
	private static final String NOT_EXIST_USERDATA_ID = "test123";

	/**
	 * Простой тест на установку текущего userdataId.
	 */
	@Test
	public void testCurUserDataIdSet() {
		AppInfoSingleton.getAppInfo().setCurUserDataIdFromMap(
				generateTestURLParamsForSL(TEST1_USERDATA));
		assertEquals(TEST1_USERDATA, AppInfoSingleton.getAppInfo().getCurUserDataId());
	}

	/**
	 * Простой тест на запуск функций из SessionUtils.
	 */
	@Test
	public void testSessionUtilsFunctions() {
		assertEquals("", SessionUtils.getCurrentSessionUserName());
		assertEquals(SessionUtils.TEST_SESSION, SessionUtils.getCurrentSessionId());
		assertEquals(SessionUtils.TEST_SID, SessionUtils.getCurrentUserSID());
	}

	/**
	 * Проверка того, что значение getCurrentUserDataId сразу после запуска
	 * равно null.
	 */
	@Test
	public void testInitialCurUserDataIdValue() {
		setDefaultUserData();
		assertEquals(ExchangeConstants.DEFAULT_USERDATA, AppInfoSingleton.getAppInfo()
				.getCurUserDataId());
	}

	/**
	 * Базовый тест на запись и чтение URLParams.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void testSessionInfoForGetChart() throws IOException, SAXException {
		Map<String, List<String>> params = generateTestURLParams(TEST1_USERDATA);

		AppInfoSingleton.getAppInfo().setAuthViaAuthServerForSession(FAKE_SESSION_ID,
				AUTH_VIA_AUTH_SERVER);
		AppInfoSingleton.getAppInfo().setAuthServerCrossAppPasswordForSession(FAKE_SESSION_ID,
				TEMP_PASS);

		CompositeContext context = getTestContext3();
		context.addSessionParams(params);
		DataPanelElementInfo element = getTestChartInfo();

		ChartGetCommand command = new ChartGetCommand(context, element);
		command.execute();

		checkTestUrlParams(context);
		assertEquals(TEST1_USERDATA, AppInfoSingleton.getAppInfo().getCurUserDataId());
		assertEquals(AUTH_VIA_AUTH_SERVER, AppInfoSingleton.getAppInfo()
				.getAuthViaAuthServerForSession(FAKE_SESSION_ID));
		assertEquals(TEMP_PASS, AppInfoSingleton.getAppInfo()
				.getAuthServerCrossAppPasswordForSession(FAKE_SESSION_ID));

	}

	private void checkTestUrlParams(final CompositeContext context) throws SAXException,
			IOException {
		String sessionContext = context.getSession();
		DocumentBuilder db = XMLUtils.createBuilder();
		Document doc = db.parse(new InputSource(new StringReader(sessionContext)));

		assertEquals(
				1,
				doc.getDocumentElement()
						.getElementsByTagName(XMLSessionContextGenerator.USERNAME_TAG).getLength());
		assertEquals(1,
				doc.getDocumentElement().getElementsByTagName(XMLSessionContextGenerator.SID_TAG)
						.getLength());
		assertEquals(SessionUtils.TEST_SID,
				doc.getDocumentElement().getElementsByTagName(XMLSessionContextGenerator.SID_TAG)
						.item(0).getChildNodes().item(0).getNodeValue());
		assertEquals(
				1,
				doc.getDocumentElement()
						.getElementsByTagName(XMLSessionContextGenerator.URL_PARAMS_TAG)
						.getLength());
		Node node =
			doc.getDocumentElement()
					.getElementsByTagName(XMLSessionContextGenerator.URL_PARAMS_TAG).item(0);
		assertEquals(XMLSessionContextGenerator.URL_PARAM_TAG, node.getChildNodes().item(1)
				.getNodeName());
		assertEquals(2, node.getChildNodes().item(1).getAttributes().getLength());
		assertEquals(KEY1, node.getChildNodes().item(1).getAttributes().getNamedItem(NAME_TAG)
				.getNodeValue());
		assertEquals("[" + VALUE12 + "]", node.getChildNodes().item(1).getAttributes()
				.getNamedItem(VALUE_TAG).getNodeValue());

		assertEquals(
				1,
				doc.getDocumentElement()
						.getElementsByTagName(XMLSessionContextGenerator.USERDATA_TAG).getLength());
		node =
			doc.getDocumentElement().getElementsByTagName(XMLSessionContextGenerator.USERDATA_TAG)
					.item(0);
		assertEquals(TEST1_USERDATA, node.getTextContent());
	}

	/**
	 * Проверка установки информации о сессии для функции получения инф. панели.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void testSessionInfoForGetDP() throws IOException, SAXException {
		Map<String, List<String>> params = generateTestURLParams(TEST1_USERDATA);
		final int elID = 5;
		Action action = getAction(TREE_MULTILEVEL_XML, 0, elID);
		action.setSessionContext(params);

		DataPanelGetCommand command = new DataPanelGetCommand(action);
		command.execute();

		assertEquals(TEST1_USERDATA, AppInfoSingleton.getAppInfo().getCurUserDataId());
		checkTestUrlParams(action.getContext());
	}

	/**
	 * Проверка считывания информации о сессии, если userdata не задана.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void testWriteAndReadIfNoURLParams() throws IOException, SAXException {
		Map<String, List<String>> params = new TreeMap<>();
		CompositeContext context = getTestContext3();
		context.addSessionParams(params);
		DataPanelElementInfo element = getTestChartInfo();

		ChartGetCommand command = new ChartGetCommand(context, element);
		command.execute();

		String sessionContext = context.getSession();
		DocumentBuilder db = XMLUtils.createBuilder();
		Document doc = db.parse(new InputSource(new StringReader(sessionContext)));
		assertEquals(
				1,
				doc.getDocumentElement()
						.getElementsByTagName(XMLSessionContextGenerator.USERDATA_TAG).getLength());
		assertEquals(ExchangeConstants.DEFAULT_USERDATA, doc.getDocumentElement()
				.getElementsByTagName(XMLSessionContextGenerator.USERDATA_TAG).item(0)
				.getTextContent());

		assertEquals(
				0,
				doc.getDocumentElement()
						.getElementsByTagName(XMLSessionContextGenerator.URL_PARAMS_TAG)
						.getLength());

	}

	/**
	 * Проверка установки и чтения текущей userdata.
	 */
	@Test
	public void testCurrentUserdata() {
		AppInfoSingleton.getAppInfo().setCurUserDataIdFromMap(
				new TreeMap<String, ArrayList<String>>());
		assertEquals(ExchangeConstants.DEFAULT_USERDATA, AppInfoSingleton.getAppInfo()
				.getCurUserDataId());
		assertNotNull(AppProps.getUserDataCatalog());

		Map<String, ArrayList<String>> params = new TreeMap<>();
		ArrayList<String> value3 = new ArrayList<>();
		value3.add(TEST1_USERDATA);
		params.put(ExchangeConstants.URL_PARAM_USERDATA, value3);
		AppInfoSingleton.getAppInfo().setCurUserDataIdFromMap(params);
		assertEquals(TEST1_USERDATA, AppInfoSingleton.getAppInfo().getCurUserDataId());
		assertNotNull(AppProps.getUserDataCatalog());
	}

	/**
	 * Проверка установки несуществующей userdata.
	 */
	@Test(expected = NoSuchUserDataException.class)
	public void testNotExistUserdata() {
		Map<String, ArrayList<String>> params = new TreeMap<>();
		ArrayList<String> value3 = new ArrayList<>();
		value3.add(NOT_EXIST_USERDATA_ID);
		params.put(ExchangeConstants.URL_PARAM_USERDATA, value3);
		AppInfoSingleton.getAppInfo().setCurUserDataIdFromMap(params);
	}

	/**
	 * Проверка вызова функции getServerCurrentState.
	 */
	@Test
	public void testGetServerCurrentState() {
		CompositeContext context = new CompositeContext();
		ServerStateGetCommand command = new ServerStateGetCommand(context);
		ServerState scs = command.execute();
		assertNotNull(scs);
		assertEquals(AppInfoSingleton.getAppInfo().getServletContainerVersion(),
				scs.getServletContainerVersion());
		assertEquals(System.getProperty("java.version"), scs.getJavaVersion());
	}

	@Test
	public void testRelatedData() throws UnsupportedEncodingException {
		CompositeContext context = CompositeContext.createCurrent();
		getExtGridContext(context);
		String res = XMLSessionContextGenerator.generate(context);
		assertTrue(res.contains("<selectedRecordId>r2</selectedRecordId>"));
		assertTrue(res.contains("<currentColumnId>curColumnId</currentColumnId>"));
		assertTrue(res.contains("size=\"2\""));
		assertTrue(res.contains("<add>value</add>"));
	}

	@Test
	public void testLastLogEventQueue() throws InterruptedException {
		testBaseLastLogEventQueue(AppInfoSingleton.getAppInfo().getLastLogEvents());
	}

	@Test
	public void testLoggingEventDecorator() {
		LoggingEventDecorator decorator = new LoggingEventDecorator(generateTestLoggingEvent());

		decorator.setUserdata(TEST1_USERDATA);
		assertTrue(decorator.isSatisfied(ExchangeConstants.URL_PARAM_USERDATA, "test1"));
		assertFalse(decorator.isSatisfied(ExchangeConstants.URL_PARAM_USERDATA, "default"));

		decorator.setUserName("master");
		assertTrue(decorator.isSatisfied("userName", "master"));
		assertFalse(decorator.isSatisfied("userName", "master1"));

		decorator.setCommandName(XFormDownloadCommand.class.getSimpleName());
		assertTrue(decorator
				.isSatisfied("commandName", XFormDownloadCommand.class.getSimpleName()));
		assertFalse(decorator.isSatisfied("commandName", XFormUploadCommand.class.getSimpleName()));

		decorator.setRequestId("1");
		assertTrue(decorator.isSatisfied("requestId", "1"));
		assertFalse(decorator.isSatisfied("requestId", "2"));

		assertTrue(decorator.isSatisfied(LEVEL_LOG_EVENT_PROP, "error"));
		assertTrue(decorator.isSatisfied(LEVEL_LOG_EVENT_PROP, "ERROR"));
		assertFalse(decorator.isSatisfied(LEVEL_LOG_EVENT_PROP, "warn"));

		assertTrue(decorator.isSatisfied("requestid", "1111"));
	}

	@Test
	public void testGetLastLogEventsWithFilter() {
		AppInfoSingleton.getAppInfo().getLastLogEvents().clear();
		LoggingEventDecorator decorator = new LoggingEventDecorator(generateTestLoggingEvent());
		decorator.setUserdata(TEST1_USERDATA);
		AppInfoSingleton.getAppInfo().addLogEvent(decorator);

		decorator = new LoggingEventDecorator(generateTestLoggingEvent());
		decorator.setUserdata("default");
		AppInfoSingleton.getAppInfo().addLogEvent(decorator);

		Map<String, List<String>> params = new TreeMap<>();
		params.put(ExchangeConstants.URL_PARAM_USERDATA, Arrays.asList(TEST1_USERDATA));
		Collection<LoggingEventDecorator> selected =
			AppInfoSingleton.getAppInfo().getLastLogEvents(params);

		assertEquals(1, selected.size());
		assertEquals(TEST1_USERDATA, selected.iterator().next().getUserdata());
	}

	@Test
	public void testGetLastLogEventsWithNullFilter() {
		AppInfoSingleton.getAppInfo().getLastLogEvents().clear();
		LoggingEventDecorator decorator = new LoggingEventDecorator(generateTestLoggingEvent());
		decorator.setUserdata(TEST1_USERDATA);
		AppInfoSingleton.getAppInfo().addLogEvent(decorator);

		Map<String, List<String>> params = null;
		Collection<LoggingEventDecorator> selected =
			AppInfoSingleton.getAppInfo().getLastLogEvents(params);

		assertEquals(1, selected.size());
		assertEquals(TEST1_USERDATA, selected.iterator().next().getUserdata());
	}

	@Test
	public void testCommandContext() {
		CommandContext cc = new CommandContext();
		cc.setUserdata(TEST1_USERDATA);
		cc.setUserName(ExchangeConstants.DEFAULT_USERDATA);
		cc.setCommandName(XFormDownloadCommand.class.getSimpleName());
		cc.setRequestId("1");

		AbstractCommandContext clone = cc.gwtClone();
		assertEquals(cc, clone);
		assertNotSame(cc, clone);
		cc.setRequestId("2");
		assertFalse(cc.equals(clone));
		assertEquals(cc.getUserName(), clone.getUserName());

		CommandContext clone2 = new CommandContext();
		clone2.assignNullValues(cc);
		assertEquals(cc, clone2);
		assertNotSame(cc, clone2);
	}
}
