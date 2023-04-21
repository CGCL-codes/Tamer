package br.usp.pcs.smarthouse.vraptor.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import junit.framework.Assert;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import br.usp.pcs.smarthouse.dao.DataController;
import br.usp.pcs.smarthouse.dao.FoodDao;
import br.usp.pcs.smarthouse.model.larder.FoodProduct;

public class KitchenLogicTest {

    private KitchenLogic assistant;

    private Mockery mockery;

    private DataController dataMock;

    private FoodDao daoMock;

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        dataMock = mockery.mock(DataController.class);
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        daoMock = mockery.mock(FoodDao.class);
        this.assistant = new KitchenLogic(dataMock);
    }

    @After
    public void assertIsSatisfied() {
        mockery.assertIsSatisfied();
    }

    @Test
    public void shouldGiveListOfInvalidProducts() {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        final List<FoodProduct> expected = new ArrayList<FoodProduct>();
        final Calendar date = Calendar.getInstance();
        mockery.checking(new Expectations() {

            {
                one(dataMock).getFoodDao();
                will(returnValue(daoMock));
                one(daoMock).listInvalidProducts(with(same(date)));
                will(returnValue(expected));
            }
        });
        List<FoodProduct> products = assistant.listInvalidProducts(date);
        Assert.assertEquals(expected, products);
    }

    @Test
    public void shouldAddFoodProduct() {
        final FoodProduct fp = mockery.mock(FoodProduct.class);
        mockery.checking(new Expectations() {

            {
                one(dataMock).getFoodDao().save(with(same(fp)));
                ignoring(fp);
            }
        });
        assistant.add(fp);
    }

    @Test
    public void shouldMarkAnFoodProductAsOpened() {
        final FoodProduct fp = new FoodProduct();
        fp.setId(1L);
        mockery.checking(new Expectations() {

            {
                one(dataMock).getFoodDao();
                will(returnValue(daoMock));
                one(daoMock).load(fp.getId());
                one(daoMock).persist(with(any(FoodProduct.class)));
            }
        });
        assistant.productOpened(fp);
    }
}
