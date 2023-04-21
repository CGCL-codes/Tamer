package no.ugland.utransprod.gui.handlers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.ugland.utransprod.gui.Login;
import no.ugland.utransprod.gui.WindowInterface;
import no.ugland.utransprod.gui.model.OrderModel;
import no.ugland.utransprod.gui.model.Packable;
import no.ugland.utransprod.model.ArticleType;
import no.ugland.utransprod.model.Colli;
import no.ugland.utransprod.model.ConstructionTypeArticle;
import no.ugland.utransprod.model.OrdchgrHeadV;
import no.ugland.utransprod.model.OrdchgrLineV;
import no.ugland.utransprod.model.OrdchgrLineVPK;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.model.OrderLine;
import no.ugland.utransprod.model.Ordln;
import no.ugland.utransprod.model.OrdlnPK;
import no.ugland.utransprod.model.UserType;
import no.ugland.utransprod.service.ApplicationParamManager;
import no.ugland.utransprod.service.ColliManager;
import no.ugland.utransprod.service.ManagerRepository;
import no.ugland.utransprod.service.OrdchgrHeadVManager;
import no.ugland.utransprod.service.OrderLineManager;
import no.ugland.utransprod.service.UserTypeManager;
import no.ugland.utransprod.service.VismaFileCreator;
import no.ugland.utransprod.service.impl.VismaFileCreatorImpl;
import no.ugland.utransprod.util.ApplicationParamUtil;
import no.ugland.utransprod.util.UserUtil;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArticlePackerTest {

    @Mock
    private OrdchgrHeadVManager ordchgrHeadVManager;

    @Mock
    private ColliViewHandlerProvider colliViewHandlerProvider;

    @Mock
    private UserTypeManager userTypeManager;

    @Mock
    private Login login;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ColliManager colliManager;

    @Mock
    private ApplicationParamManager applicationParamManager;

    private Packable packable;

    private OrderLine orderLine;

    @Mock
    private OrderLineManager orderLineManager;

    @BeforeMethod
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ApplicationParamUtil.setApplicationParamManger(applicationParamManager);
        when(managerRepository.getColliManager()).thenReturn(colliManager);
        when(managerRepository.getOrderLineManager()).thenReturn(orderLineManager);
        UserType userType = new UserType();
        userType.setIsAdmin(1);
        when(login.getUserType()).thenReturn(userType);
        final OrdchgrHeadV ordchgrHeadV = new OrdchgrHeadV();
        when(ordchgrHeadVManager.getHead(1)).thenReturn(ordchgrHeadV);
        final List<Integer> lnNos = new ArrayList<Integer>();
        lnNos.add(2);
        final List<OrdchgrLineV> ordlns = new ArrayList<OrdchgrLineV>();
        OrdchgrLineV ordchgrLineV = new OrdchgrLineV();
        ordchgrLineV.setOrdchgrLineVPK(new OrdchgrLineVPK(1, 2));
        ordchgrLineV.setLineStatus(10);
        ordlns.add(ordchgrLineV);
        when(ordchgrHeadVManager.getLines(1, lnNos)).thenReturn(ordlns);
        when(applicationParamManager.findByName("visma_out_dir")).thenReturn("visma");
        when(applicationParamManager.findByName("not_package")).thenReturn("");
        final Colli colli = new Colli();
        colli.setColliName("Port");
        orderLine = new OrderLine();
        orderLine.setOrdNo(1);
        Ordln ordln = new Ordln();
        ordln.setOrdlnPK(new OrdlnPK(2, 1));
        orderLine.setOrdln(ordln);
        orderLine.setLnNo(2);
        ConstructionTypeArticle constructionTypeArticle = new ConstructionTypeArticle();
        ArticleType articleType = new ArticleType();
        articleType.setArticleTypeName("Port");
        constructionTypeArticle.setArticleType(articleType);
        orderLine.setConstructionTypeArticle(constructionTypeArticle);
        final Order order = new Order();
        order.setOrderNr("100");
        order.addOrderLine(orderLine);
        order.addColli(colli);
        packable = new OrderModel(order, false, false, false, null, null);
        final ColliViewHandler colliViewHandler = new ColliViewHandler(null, colli, packable, login, managerRepository, null);
        when(colliViewHandlerProvider.getColliViewHandler(colli)).thenReturn(colliViewHandler);
    }

    @AfterMethod
    protected void tearDown() throws Exception {
    }

    @Test
    public void testPackAndSendFileToVisma() throws Exception {
        Map<String, String> colliSetup = new HashMap<String, String>();
        colliSetup.put("Port", "Port");
        VismaFileCreator vismaFileCreator = new VismaFileCreatorImpl(ordchgrHeadVManager, false);
        ArticlePacker articlePacker = new ArticlePacker(colliViewHandlerProvider, colliSetup, vismaFileCreator);
        UserUtil.setUserTypeManagerForTest(userTypeManager);
        WindowInterface window = null;
        articlePacker.packOrderLine(orderLine, packable, window, true);
        assertNotNull(orderLine.getColli());
        assertEquals("Port", orderLine.getColli().getColliName());
        File file = new File("visma/100_.edi");
        assertEquals(true, file.exists());
    }
}
