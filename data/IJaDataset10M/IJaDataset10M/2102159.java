package org.netxilia.spi.impl.structure;

import org.junit.Assert;
import org.junit.Test;
import org.netxilia.api.INetxiliaSystem;
import org.netxilia.api.exception.NetxiliaBusinessException;
import org.netxilia.api.exception.NetxiliaResourceException;
import org.netxilia.api.formula.CyclicDependenciesException;
import org.netxilia.api.formula.Formula;
import org.netxilia.api.impl.NetxiliaSystemImpl;
import org.netxilia.api.model.CellData;
import org.netxilia.api.model.ISheet;
import org.netxilia.api.model.SheetType;
import org.netxilia.api.model.SortSpecifier;
import org.netxilia.api.model.WorkbookId;
import org.netxilia.api.reference.CellReference;
import org.netxilia.api.user.AclPrivilegedMode;
import org.netxilia.api.value.NumberValue;
import org.netxilia.api.value.StringValue;
import org.netxilia.spi.impl.storage.memory.InMemoryWorkbookStorageServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSheet {

    @Test
    public void testSimpleSetGet() throws NetxiliaResourceException, NetxiliaBusinessException {
        INetxiliaSystem processor = getWorkbookProcessor();
        ISheet sheet = processor.getWorkbook(new WorkbookId("workbookTest")).addNewSheet("test", SheetType.normal);
        CellReference ref = new CellReference(0, 0);
        sheet.sendValue(ref, new NumberValue(100));
        ISheet rdsheet = processor.getWorkbook(new WorkbookId("workbookTest")).getSheet("test");
        Assert.assertNotNull(rdsheet);
        CellData rdcell = rdsheet.receiveCell(ref).getNonBlocking();
        Assert.assertNotNull(rdcell);
        Assert.assertNotNull(rdcell.getValue());
        Assert.assertEquals(100, rdcell.getValue().getNumberValue(), 0.1);
    }

    @Test
    public void testSetFormula() throws NetxiliaResourceException, NetxiliaBusinessException {
        INetxiliaSystem processor = getWorkbookProcessor();
        ISheet sheet = processor.getWorkbook(new WorkbookId("workbookTest")).addNewSheet("test", SheetType.normal);
        CellReference ref = new CellReference(0, 0);
        CellReference ref2 = new CellReference(0, 1);
        sheet.sendValue(ref, new NumberValue(100));
        sheet.sendFormula(ref2, new Formula("=A1 + 10"));
        ISheet rdsheet = processor.getWorkbook(new WorkbookId("workbookTest")).getSheet("test");
        Assert.assertNotNull(rdsheet);
        CellData rdcell = rdsheet.receiveCell(ref2).getNonBlocking();
        Assert.assertNotNull(rdcell);
        Assert.assertNotNull(rdcell.getValue());
        Assert.assertEquals(110, rdcell.getValue().getNumberValue(), 0.1);
        Assert.assertEquals("=A1 + 10", rdcell.getFormula().getFormula());
    }

    @Test
    public void testSetFormulaAndRemove() throws NetxiliaResourceException, NetxiliaBusinessException {
        INetxiliaSystem processor = getWorkbookProcessor();
        ISheet sheet = processor.getWorkbook(new WorkbookId("workbookTest")).addNewSheet("test", SheetType.normal);
        CellReference ref = new CellReference(0, 0);
        CellReference ref2 = new CellReference(0, 1);
        sheet.sendValue(ref, new NumberValue(100));
        sheet.sendFormula(ref2, new Formula("=A1 + 10"));
        sheet.sendValue(ref2, new StringValue(""));
        ISheet rdsheet = processor.getWorkbook(new WorkbookId("workbookTest")).getSheet("test");
        Assert.assertNotNull(rdsheet);
        CellData rdcell = rdsheet.receiveCell(ref2).getNonBlocking();
        Assert.assertNotNull(rdcell);
        Assert.assertNotNull(rdcell.getValue());
        Assert.assertEquals("", rdcell.getValue().getStringValue());
        Assert.assertNull(rdcell.getFormula());
    }

    @Test
    public void testSetFormulaDirty() throws NetxiliaResourceException, NetxiliaBusinessException {
        INetxiliaSystem processor = getWorkbookProcessor();
        ISheet sheet = processor.getWorkbook(new WorkbookId("workbookTest")).addNewSheet("test", SheetType.normal);
        CellReference ref = new CellReference(0, 0);
        CellReference ref2 = new CellReference(0, 1);
        sheet.sendValue(ref, new NumberValue(100));
        sheet.sendFormula(ref2, new Formula("=A1 + 10"));
        sheet.sendValue(ref, new NumberValue(200));
        ISheet rdsheet = processor.getWorkbook(new WorkbookId("workbookTest")).getSheet("test");
        Assert.assertNotNull(rdsheet);
        CellData rdcell = rdsheet.receiveCell(ref2).getNonBlocking();
        Assert.assertNotNull(rdcell);
        Assert.assertNotNull(rdcell.getValue());
        Assert.assertEquals(210, rdcell.getValue().getNumberValue(), 0.1);
        Assert.assertEquals("=A1 + 10", rdcell.getFormula().getFormula());
    }

    @Test
    public void testSetFormulaWithCycle() throws NetxiliaResourceException, NetxiliaBusinessException {
        INetxiliaSystem processor = getWorkbookProcessor();
        ISheet sheet = processor.getWorkbook(new WorkbookId("workbookTest")).addNewSheet("test", SheetType.normal);
        CellReference ref = new CellReference("A1");
        CellReference ref2 = new CellReference("B1");
        CellReference ref3 = new CellReference("C1");
        sheet.sendValue(ref, new NumberValue(100));
        sheet.sendFormula(ref2, new Formula("=A1 + 10"));
        sheet.sendFormula(ref3, new Formula("=B1 + 10"));
        try {
            sheet.sendFormula(ref, new Formula("=C1 + 10")).getNonBlocking();
            Assert.fail("Exception was not thrown");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof CyclicDependenciesException);
        }
    }

    @Test
    public void testSort() throws NetxiliaResourceException, NetxiliaBusinessException {
        INetxiliaSystem processor = getWorkbookProcessor();
        ISheet sheet = processor.getWorkbook(new WorkbookId("workbookTest")).addNewSheet("test", SheetType.normal);
        CellReference ref1 = new CellReference("A1");
        CellReference ref2 = new CellReference("A2");
        CellReference ref3 = new CellReference("A3");
        sheet.sendValue(ref1, new NumberValue(100));
        sheet.sendFormula(ref2, new Formula("=A1 + 2"));
        sheet.sendValue(ref3, new NumberValue(80));
        sheet.sort(new SortSpecifier("A"));
        ISheet rdsheet = processor.getWorkbook(new WorkbookId("workbookTest")).getSheet("test");
        Assert.assertNotNull(rdsheet);
        CellData rdcell1 = rdsheet.receiveCell(ref1).getNonBlocking();
        Assert.assertNotNull(rdcell1);
        Assert.assertNotNull(rdcell1.getValue());
        Assert.assertEquals(80, rdcell1.getValue().getNumberValue(), 0.1);
        CellData rdcell2 = rdsheet.receiveCell(ref2).getNonBlocking();
        Assert.assertNotNull(rdcell2);
        Assert.assertNotNull(rdcell2.getValue());
        Assert.assertEquals(100, rdcell2.getValue().getNumberValue(), 0.1);
        CellData rdcell3 = rdsheet.receiveCell(ref3).getNonBlocking();
        Assert.assertNotNull(rdcell3);
        Assert.assertNotNull(rdcell3.getValue());
        Assert.assertEquals(102, rdcell3.getValue().getNumberValue(), 0.1);
    }

    @Test
    public void testInit() throws NetxiliaResourceException, NetxiliaBusinessException {
        AclPrivilegedMode.set();
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:test-domain-services.xml");
        INetxiliaSystem processor = context.getBean(NetxiliaSystemImpl.class);
        ISheet sheet = processor.getWorkbook(new WorkbookId("SYSTEM")).getSheet("testFunc");
        MyFunctions.SET_TEST_VALUE(100);
        CellData cell = sheet.receiveCell(new CellReference("A2")).getNonBlocking();
        Assert.assertNotNull(cell);
        Assert.assertNotNull(cell.getValue());
        Assert.assertEquals(101, cell.getValue().getNumberValue(), 0.1);
        AclPrivilegedMode.clear();
    }

    private INetxiliaSystem getWorkbookProcessor() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:test-domain-services.xml");
        NetxiliaSystemImpl processor = context.getBean(NetxiliaSystemImpl.class);
        processor.setAclService(new NoCheckAclServiceImpl());
        InMemoryWorkbookStorageServiceImpl storageService = new InMemoryWorkbookStorageServiceImpl();
        storageService.setWorkbookProcessor(processor);
        storageService.setApplicationContext(context);
        processor.setStorageService(storageService);
        return processor;
    }
}
