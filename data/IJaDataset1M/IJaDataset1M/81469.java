package ru.curs.showcase.test;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;

import ru.curs.gwt.datagrid.model.*;
import ru.curs.showcase.app.api.ExchangeConstants;
import ru.curs.showcase.app.api.datapanel.*;
import ru.curs.showcase.app.api.event.*;
import ru.curs.showcase.app.api.grid.GridContext;
import ru.curs.showcase.app.api.navigator.Navigator;
import ru.curs.showcase.app.server.AppInitializer;
import ru.curs.showcase.model.datapanel.*;
import ru.curs.showcase.model.navigator.*;
import ru.curs.showcase.runtime.*;
import ru.curs.showcase.util.*;
import ru.curs.showcase.util.xml.GeneralXMLHelper;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * Класс абстрактного теста, использующего тестовые файлы с данными.
 * 
 * @author den
 * 
 */
public class AbstractTest extends GeneralXMLHelper {

	private static final String TEST_GOOD_XSD = "test_good.xsd";
	private static final String TEST_GOOD_SMALL_XSD = "test_good_small.xsd";
	private static final String XFORMS_DOWNLOAD2 = "xforms_download2";
	protected static final String TEST_GOOD_XSL = "test_good.xsl";
	protected static final String XFORMS_UPLOAD1 = "xforms_upload1";
	protected static final String TEST_XML = "test.xml";
	protected static final String TEST1_1_XML = "test1.1.xml";
	protected static final String TEST2_XML = "test2.xml";
	protected static final String TEST1_USERDATA = "test1";
	protected static final String TEST2_USERDATA = "test2";
	protected static final String VALUE12 = "value1";
	protected static final String KEY1 = "key1";

	protected static final String TREE_MULTILEVEL_XML = "tree_multilevel.xml";
	protected static final String TREE_MULTILEVEL_V2_XML = "tree_multilevel.v2.xml";

	protected static final String ADD_CONDITION = "add_condition";
	protected static final String TEST_XML_FILE = "logic.xml";

	protected static final String RICH_DP = "m1003.xml";

	protected static final String GEOMAP_WOHEADER_SVG = "geomap_woheader.svg";
	protected static final String RU_CURS_SHOWCASE_TEST = "ru\\curs\\showcase\\test\\";

	/**
	 * Действия, которые должны выполняться перед запуском любых тестовых
	 * классов.
	 */
	@BeforeClass
	public static void beforeClass() {
		AppInitializer.initialize();
		if (AppInfoSingleton.getAppInfo().getUserdatas().size() == 0) {
			AppInitializer.readPathProperties();
		}
		if (AppInfoSingleton.getAppInfo().getUserdatas().size() == 0) {
			throw new NoRootPathUserDataException();
		}
		initTestSession();
	}

	/**
	 * Очистка информации о текущей userdata после каждого теста.
	 */
	@After
	public void afterTest() {
		resetUserData();
	}

	protected void resetUserData() {
		AppInfoSingleton.getAppInfo().setCurUserDataId((String) null);
		AppInfoSingleton.getAppInfo().clearSessions();
	}

	private static void initTestSession() {
		AppInfoSingleton.getAppInfo().clearSessions();
		AppInfoSingleton.getAppInfo().addSession(ServletUtils.TEST_SESSION);
	}

	/**
	 * Возвращает элемент информационной панели для тестов.
	 * 
	 * @param fileName
	 *            - файл панели.
	 * @param tabID
	 *            - идентификатор вкладки.
	 * @param elID
	 *            - идентификатор элемента.
	 * @return элемент.
	 */
	protected DataPanelElementInfo getDPElement(final String fileName, final String tabID,
			final String elID) {
		boolean needReset = false;
		if (AppInfoSingleton.getAppInfo().getCurUserDataId() == null) {
			setDefaultUserData();
			needReset = true;
		}
		try {
			DataPanelGateway gateway = new DataPanelFileGateway();
			DataFile<InputStream> file = gateway.getRawData(new CompositeContext(), fileName);
			DataPanelFactory dpFactory = new DataPanelFactory();
			DataPanel panel = dpFactory.fromStream(file);
			DataPanelElementInfo element = panel.getTabById(tabID).getElementInfoById(elID);
			assertTrue(element.isCorrect());
			return element;
		} finally {
			if (needReset) {
				resetUserData();
			}
		}
	}

	protected void setDefaultUserData() {
		AppInfoSingleton.getAppInfo().setCurUserDataId(ExchangeConstants.DEFAULT_USERDATA);
	}

	/**
	 * Генерирует описание грида для тестов.
	 * 
	 * @return DataPanelElementInfo
	 * 
	 */
	protected DataPanelElementInfo getTestGridInfo() {
		DataPanelElementInfo elInfo = new DataPanelElementInfo("2", DataPanelElementType.GRID);
		elInfo.setPosition(1);
		elInfo.setProcName("grid_bal");
		generateTestTabWithElement(elInfo);
		assertTrue(elInfo.isCorrect());
		return elInfo;
	}

	/**
	 * Генерирует описание грида для тестов.
	 * 
	 * @return DataPanelElementInfo
	 * 
	 */
	protected DataPanelElementInfo getTestGridInfo2() {
		DataPanelElementInfo elInfo = new DataPanelElementInfo("2", DataPanelElementType.GRID);
		elInfo.setPosition(1);
		elInfo.setProcName("grid_cities_data");
		generateTestTabWithElement(elInfo);
		DataPanelElementProc proc = new DataPanelElementProc();
		proc.setId("2md");
		proc.setName("grid_cities_metadata");
		proc.setType(DataPanelElementProcType.METADATA);
		elInfo.getProcs().put(proc.getId(), proc);
		assertTrue(elInfo.isCorrect());
		return elInfo;
	}

	protected void generateTestTabWithElement(final DataPanelElementInfo elInfo) {
		DataPanel dp = new DataPanel("xxx");
		DataPanelTab tab = new DataPanelTab(0, dp);
		tab.add(elInfo);
	}

	/**
	 * Генерирует описание графика для тестов.
	 * 
	 * @return DataPanelElementInfo
	 */
	protected DataPanelElementInfo getTestChartInfo() {
		DataPanelElementInfo elInfo = new DataPanelElementInfo("3", DataPanelElementType.CHART);
		elInfo.setPosition(2);
		elInfo.setProcName("chart_bal");
		elInfo.setHideOnLoad(true);
		generateTestTabWithElement(elInfo);

		assertTrue(elInfo.isCorrect());
		return elInfo;
	}

	/**
	 * Генерирует описание xforms для тестов.
	 * 
	 * @return DataPanelElementInfo
	 */
	protected DataPanelElementInfo getTestXForms1Info() {
		DataPanelElementInfo elInfo = new DataPanelElementInfo("08", DataPanelElementType.XFORMS);
		final int position = 6;
		elInfo.setPosition(position);
		elInfo.setProcName("xforms_proc1");
		elInfo.setTemplateName("Showcase_Template.xml");

		DataPanelElementProc proc = new DataPanelElementProc();
		proc.setId("proc1");
		proc.setName("xforms_saveproc1");
		proc.setType(DataPanelElementProcType.SAVE);
		elInfo.getProcs().put(proc.getId(), proc);
		proc = new DataPanelElementProc();
		proc.setId("proc2");
		proc.setName("xforms_submission1");
		proc.setType(DataPanelElementProcType.SUBMISSION);
		elInfo.getProcs().put(proc.getId(), proc);

		generateTestTabWithElement(elInfo);

		assertTrue(elInfo.isCorrect());
		return elInfo;
	}

	/**
	 * Генерирует описание xforms для тестов.
	 * 
	 * @return DataPanelElementInfo
	 */
	protected DataPanelElementInfo getTestXForms2Info() {
		DataPanelElementInfo elInfo = new DataPanelElementInfo("09", DataPanelElementType.XFORMS);
		final int position = 7;
		elInfo.setPosition(position);
		elInfo.setProcName("xforms_proc1");
		elInfo.setTemplateName("Showcase_Template.xml");

		DataPanelElementProc proc = new DataPanelElementProc();
		proc.setId("proc3");
		proc.setName("xforms_save_error_proc1");
		proc.setType(DataPanelElementProcType.SAVE);
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc4");
		proc.setName("xforms_download1");
		proc.setType(DataPanelElementProcType.DOWNLOAD);
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc5");
		proc.setName(XFORMS_UPLOAD1);
		proc.setType(DataPanelElementProcType.UPLOAD);
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc6");
		proc.setName(XFORMS_DOWNLOAD2);
		proc.setType(DataPanelElementProcType.DOWNLOAD);
		proc.setSchemaName(TEST_GOOD_SMALL_XSD);
		proc.setTransformName(TEST_GOOD_XSL);
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc6jj");
		proc.setName(XFORMS_DOWNLOAD2);
		proc.setType(DataPanelElementProcType.DOWNLOAD);
		proc.setSchemaName("schema/TestGoodSmall.py");
		proc.setTransformName("transform/TestGood.py");
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc6spsp");
		proc.setName(XFORMS_DOWNLOAD2);
		proc.setType(DataPanelElementProcType.DOWNLOAD);
		proc.setSchemaName("xformSchemaTestGoodSmall");
		proc.setTransformName("xformTransformTestGood");
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc7");
		proc.setName(XFORMS_UPLOAD1);
		proc.setType(DataPanelElementProcType.UPLOAD);
		proc.setSchemaName(TEST_GOOD_XSD);
		proc.setTransformName(TEST_GOOD_XSL);
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc7jj");
		proc.setName(XFORMS_UPLOAD1);
		proc.setType(DataPanelElementProcType.UPLOAD);
		proc.setSchemaName("schema/TestGood.py");
		proc.setTransformName("transform/TestGood.py");
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc7spsp");
		proc.setName(XFORMS_UPLOAD1);
		proc.setType(DataPanelElementProcType.UPLOAD);
		proc.setSchemaName("xformSchemaTestGood");
		proc.setTransformName("xformTransformTestGood");
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc8");
		proc.setName(XFORMS_UPLOAD1);
		proc.setType(DataPanelElementProcType.UPLOAD);
		proc.setSchemaName("test_bad.xsd");
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc9");
		proc.setName(XFORMS_UPLOAD1);
		proc.setType(DataPanelElementProcType.UPLOAD);
		proc.setTransformName(TEST_GOOD_XSL);
		elInfo.getProcs().put(proc.getId(), proc);

		proc = new DataPanelElementProc();
		proc.setId("proc10");
		proc.setName("xforms_download3_wrong");
		proc.setType(DataPanelElementProcType.DOWNLOAD);
		proc.setSchemaName(TEST_GOOD_SMALL_XSD);
		elInfo.getProcs().put(proc.getId(), proc);

		generateTestTabWithElement(elInfo);

		assertTrue(elInfo.isCorrect());
		return elInfo;
	}

	/**
	 * Возвращает контекст для тестов из файла навигатора.
	 * 
	 * @param groupID
	 *            - номер группы в файле.
	 * @param elID
	 *            - номер элемента в группе.
	 * @param fileName
	 *            - имя файла с навигатором.
	 * @return - контекст.
	 */
	protected CompositeContext
			getContext(final String fileName, final int groupID, final int elID) {
		CompositeContext context = getAction(fileName, groupID, elID).getContext();
		return context;
	}

	/**
	 * Возвращает контекст для тестов.
	 * 
	 * @return - контекст.
	 */
	protected CompositeContext getTestContext1() {
		CompositeContext context = new CompositeContext();
		context.setMain("Ввоз, включая импорт - Всего");
		return context;
	}

	protected GridContext getTestGridContext1() {
		CompositeContext context = getTestContext1();
		GridContext gc = GridContext.createFirstLoadDefault();
		gc.apply(context);
		return gc;
	}

	protected GridContext getExtGridContext(final CompositeContext context) {
		GridContext gc = new GridContext();
		gc.setAdditional("<add>value</add>");
		gc.setFilter("<filter>filter_value</filter>");
		gc.setPageSize(2);
		Column col = new Column();
		col.setId("colId");
		col.setSorting(Sorting.ASC);
		col.setWidth("10px");
		gc.getSortedColumns().add(col);
		gc.getSelectedRecordIds().add("r1");
		gc.getSelectedRecordIds().add("r2");
		gc.setCurrentColumnId("curColumnId");
		gc.setCurrentRecordId("curRecordId");

		context.addRelated("01", gc);

		return gc;
	}

	/**
	 * Возвращает контекст для тестов.
	 * 
	 * @return - контекст.
	 */
	protected CompositeContext getTestContext2() {
		CompositeContext context = new CompositeContext();
		context.setMain("Алтайский край");
		return context;
	}

	/**
	 * Возвращает контекст для тестов.
	 * 
	 * @return - контекст.
	 */
	protected CompositeContext getTestContext3() {
		CompositeContext context = new CompositeContext();
		context.setMain("Межрегиональный обмен - Всего");
		context.setAdditional("Алтайский край");
		return context;
	}

	/**
	 * Возвращает действие для тестов из файла навигатора.
	 * 
	 * @param groupID
	 *            - номер группы в файле.
	 * @param elID
	 *            - номер элемента в группе.
	 * @param fileName
	 *            - имя файла с навигатором.
	 * @return - контекст.
	 */
	protected Action getAction(final String fileName, final int groupID, final int elID) {
		boolean needReset = false;
		if (AppInfoSingleton.getAppInfo().getCurUserDataId() == null) {
			setDefaultUserData();
			needReset = true;
		}
		try (PrimaryElementsGateway gateway = new NavigatorFileGateway()) {
			InputStream stream1 = gateway.getRawData(new CompositeContext(), fileName);
			CompositeContext context =
				new CompositeContext(generateTestURLParams(ExchangeConstants.DEFAULT_USERDATA));
			NavigatorFactory navFactory = new NavigatorFactory(context);
			Navigator nav = navFactory.fromStream(stream1);
			Action action = nav.getGroups().get(groupID).getElements().get(elID).getAction();
			return action;
		} finally {
			if (needReset) {
				resetUserData();
			}
		}
	}

	/**
	 * Генерирует набор параметров URL c заданной userdata.
	 * 
	 * @param userDataId
	 *            - идентификатор userdata.
	 */
	protected Map<String, List<String>> generateTestURLParams(final String userDataId) {
		Map<String, List<String>> params = new TreeMap<>();
		ArrayList<String> value1 = new ArrayList<>();
		value1.add(VALUE12);
		params.put(KEY1, value1);
		ArrayList<String> value2 = new ArrayList<>();
		value2.add("value21");
		value2.add("value22");
		params.put("key2", value2);
		ArrayList<String> value3 = new ArrayList<>();
		value3.add(userDataId);
		params.put(ExchangeConstants.URL_PARAM_USERDATA, value3);
		return params;
	}

	/**
	 * Генерирует набор параметров URL c заданной userdata.
	 * 
	 * @param userDataId
	 *            - идентификатор userdata.
	 */
	protected Map<String, ArrayList<String>> generateTestURLParamsForSL(final String userDataId) {
		Map<String, ArrayList<String>> params = new TreeMap<>();
		ArrayList<String> value1 = new ArrayList<>();
		value1.add(VALUE12);
		params.put(KEY1, value1);
		ArrayList<String> value2 = new ArrayList<>();
		value2.add("value21");
		value2.add("value22");
		params.put("key2", value2);
		ArrayList<String> value3 = new ArrayList<>();
		value3.add(userDataId);
		params.put(ExchangeConstants.URL_PARAM_USERDATA, value3);
		return params;
	}

	protected void testBaseLastLogEventQueue(final Collection<LoggingEventDecorator> lleq)
			throws InterruptedException {
		AppInfoSingleton.getAppInfo().setCurUserDataId(ExchangeConstants.DEFAULT_USERDATA);
		final int eventCount = 405;
		for (int i = 0; i < eventCount; i++) {
			Thread.sleep(1);
			LoggingEvent original = generateTestLoggingEvent();
			lleq.add(new LoggingEventDecorator(original));
		}

		assertEquals(LastLogEvents.getMaxRecords(), lleq.size());
	}

	protected LoggingEvent generateTestLoggingEvent() {
		LoggingEvent event = new LoggingEvent();
		event.setMessage("message");
		event.setLevel(Level.ERROR);
		Date date = new Date();
		event.setTimeStamp(date.getTime());
		return event;
	}

	protected OutputStreamDataFile getTestFile(final String linkId) throws IOException {
		OutputStreamDataFile file =
			new OutputStreamDataFile(StreamConvertor.inputToOutputStream(FileUtils
					.loadResToStream(linkId)), linkId);
		return file;
	}

	protected CompositeContext generateContextWithSessionInfo() {
		return new CompositeContext(generateTestURLParams(ExchangeConstants.DEFAULT_USERDATA));
	}
}