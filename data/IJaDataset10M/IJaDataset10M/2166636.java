package uk.ac.ed.rapid.xml.test;

import java.net.URL;
import java.util.Collection;
import java.util.Vector;
import junit.framework.TestCase;
import org.mockito.Mockito;
import uk.ac.ed.rapid.data.RapidData;
import uk.ac.ed.rapid.job.impl.JobImpl;
import uk.ac.ed.rapid.jobdata.JobData;
import uk.ac.ed.rapid.jobdata.JobDataImpl;
import uk.ac.ed.rapid.jobdata.JobID;
import uk.ac.ed.rapid.selection.Selection;
import uk.ac.ed.rapid.selection.SelectionTable;
import uk.ac.ed.rapid.symbol.Symbol;
import uk.ac.ed.rapid.symbol.SymbolTable;
import uk.ac.ed.rapid.symbol.impl.CheckBoxSymbol;
import uk.ac.ed.rapid.symbol.impl.FileBrowserSymbol;
import uk.ac.ed.rapid.symbol.impl.FileUploadSymbol;
import uk.ac.ed.rapid.symbol.impl.SelectionSymbol;
import uk.ac.ed.rapid.symbol.impl.SubstituteVariableSymbol;
import uk.ac.ed.rapid.symbol.impl.SymbolImpl;
import uk.ac.ed.rapid.value.impl.SelectionValue;
import uk.ac.ed.rapid.xml.RapidXML;
import uk.ac.ed.rapid.xml.SelectionReader;
import uk.ac.ed.rapid.xml.SymbolTableReader;

/**
 * 
 * @author jos
 */
public class SymbolReaderTest extends TestCase {

    private static final String directory = "jobtest/";

    public void testVariableTable() {
        try {
            URL xmlURL = RapidXML.getCustomXMLFile(directory + "variableelement.xml");
            SymbolTable table = SymbolTableReader.read(xmlURL);
            assertEquals(7, table.size());
            Collection<Symbol> symbols = table.getSymbolsByPage("pagename");
            assertEquals(6, symbols.size());
            symbols = table.getSymbolsByPage("array");
            assertEquals(1, symbols.size());
            for (Symbol symbol : symbols) assertEquals("symbol3", symbol.getSymbolName());
        } catch (Exception ex) {
            fail();
        }
    }

    public void testVariableSymbol() {
        try {
            URL xmlURL = RapidXML.getCustomXMLFile(directory + "variableelement.xml");
            SymbolTable table = SymbolTableReader.read(xmlURL);
            assertEquals(7, table.size());
            SymbolImpl symbol1 = (SymbolImpl) table.getSymbol("symbol");
            assertEquals("pagename", symbol1.getPage());
            assertEquals(-1, symbol1.getSubJob());
            assertEquals("symbol", symbol1.getSymbolName());
            assertEquals(false, symbol1.isReadOnly());
            assertEquals("varname1", symbol1.getVariableName());
            SymbolImpl symbol2 = (SymbolImpl) table.getSymbol("symbol2");
            assertEquals("pagename", symbol2.getPage());
            assertEquals(-1, symbol2.getSubJob());
            assertEquals("symbol2", symbol2.getSymbolName());
            assertEquals(true, symbol2.isReadOnly());
            assertEquals("varname1", symbol2.getVariableName());
            SymbolImpl symbol3 = (SymbolImpl) table.getSymbol("symbol3");
            assertEquals("array", symbol3.getPage());
            assertEquals(-1, symbol3.getSubJob());
            assertEquals("symbol3", symbol3.getSymbolName());
            assertEquals(false, symbol3.isReadOnly());
            assertEquals("varname2", symbol3.getVariableName());
            SymbolImpl symbol4 = (SymbolImpl) table.getSymbol("symbolstep");
            assertEquals("pagename", symbol4.getPage());
            assertEquals(2, symbol4.getSubJob());
            assertEquals("symbolstep", symbol4.getSymbolName());
            assertEquals(false, symbol4.isReadOnly());
            assertEquals("varname4", symbol4.getVariableName());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    public void testFileUploadSymbol() {
        try {
            URL xmlURL = RapidXML.getCustomXMLFile(directory + "fileuploadelement.xml");
            SymbolTable table = SymbolTableReader.read(xmlURL);
            assertEquals(2, table.size());
            FileUploadSymbol symbol1 = (FileUploadSymbol) table.getSymbol("symbol1");
            assertEquals("page1", symbol1.getPage());
            assertEquals(-1, symbol1.getSubJob());
            assertEquals("symbol1", symbol1.getSymbolName());
            assertEquals("", symbol1.getVariableName());
            assertEquals("filesystem1", symbol1.getFileSystemName());
            assertEquals("path1", symbol1.getPath());
            FileUploadSymbol symbol2 = (FileUploadSymbol) table.getSymbol("symbol2");
            assertEquals("page2", symbol2.getPage());
            assertEquals(-1, symbol2.getSubJob());
            assertEquals("symbol2", symbol2.getSymbolName());
            assertEquals("uploadtwo", symbol2.getVariableName());
            assertEquals("filesystem2", symbol2.getFileSystemName());
            assertEquals("path2", symbol2.getPath());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    public void testSelectionSymbol() {
        try {
            URL xmlURL = RapidXML.getCustomXMLFile(directory + "jobselection.xml");
            SymbolTable table = SymbolTableReader.read(xmlURL);
            SelectionTable selectionTable = SelectionReader.read(xmlURL);
            assertEquals(2, table.size());
            JobData jobData = new JobDataImpl(new JobImpl());
            RapidData rapidData = Mockito.mock(RapidData.class);
            Mockito.when(rapidData.getJobData()).thenReturn(jobData);
            Mockito.when(rapidData.getSelectionTable()).thenReturn(selectionTable);
            SelectionSymbol symbol1 = (SelectionSymbol) table.getSymbol("symbol1");
            assertEquals("pagename", symbol1.getPage());
            assertEquals(-1, symbol1.getSubJob());
            assertEquals("symbol1", symbol1.getSymbolName());
            assertEquals("jobselection1", symbol1.getVariableName());
            SelectionSymbol symbol2 = (SelectionSymbol) table.getSymbol("symbol2");
            assertEquals("pagename", symbol2.getPage());
            assertEquals(-1, symbol2.getSubJob());
            assertEquals("symbol2", symbol2.getSymbolName());
            assertEquals("jobselection2", symbol2.getVariableName());
            assertEquals("", selectionTable.getSelection("jobselection1").get());
            assertTrue(selectionTable.getSelection("jobselection1") instanceof SelectionValue);
            Vector<String> input = new Vector<String>();
            String serialisedJobID = new Selection(new JobID().initialise(), 0).serialise();
            input.add(serialisedJobID);
            symbol1.put(rapidData, input);
            assertEquals(serialisedJobID, selectionTable.getSelection("jobselection1").getSelection(0).serialise());
            assertEquals(2, table.getSymbolsByPage("pagename").size());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    public void testSymbolTypes() {
        try {
            URL xmlURL = RapidXML.getCustomXMLFile(directory + "symbols.xml");
            SymbolTable table = SymbolTableReader.read(xmlURL);
            assertEquals(5, table.size());
            assertTrue(table.getSymbol("symbol1") instanceof SymbolImpl);
            SymbolImpl symbol1 = (SymbolImpl) table.getSymbol("symbol1");
            assertEquals("varname1", symbol1.getVariableName());
            assertTrue(table.getSymbol("symbol2") instanceof SymbolImpl);
            SymbolImpl symbol2 = (SymbolImpl) table.getSymbol("symbol2");
            assertEquals("varname2", symbol2.getVariableName());
            assertTrue(table.getSymbol("symbol3") instanceof SymbolImpl);
            SymbolImpl symbol3 = (SymbolImpl) table.getSymbol("symbol3");
            assertEquals("varname3", symbol3.getVariableName());
            assertTrue(table.getSymbol("symbol4") instanceof CheckBoxSymbol);
            CheckBoxSymbol symbol4 = (CheckBoxSymbol) table.getSymbol("symbol4");
            assertEquals("varname4", symbol4.getVariableName());
            assertEquals("true", symbol4.getChecked());
            assertEquals("false", symbol4.getUnchecked());
            assertTrue(table.getSymbol("symbol5") instanceof SubstituteVariableSymbol);
            SubstituteVariableSymbol symbol5 = (SubstituteVariableSymbol) table.getSymbol("symbol5");
            assertEquals("varname5", symbol5.getVariableName());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    public void testFileBrowserSymbol() {
        try {
            URL xmlURL = RapidXML.getCustomXMLFile(directory + "pathelement.xml");
            SymbolTable table = SymbolTableReader.read(xmlURL);
            Symbol symbol;
            FileBrowserSymbol fileBrowserSymbol;
            assertEquals(4, table.size());
            symbol = table.getSymbol("symbol1" + FileBrowserSymbol.DOUBLECLICK);
            assertTrue(symbol instanceof FileBrowserSymbol);
            fileBrowserSymbol = (FileBrowserSymbol) symbol;
            assertEquals("pagename", fileBrowserSymbol.getPage());
            assertEquals("symbol1" + FileBrowserSymbol.DOUBLECLICK, fileBrowserSymbol.getSymbolName());
            assertEquals("symbol1", fileBrowserSymbol.getFileBrowserName());
            assertEquals("path1", fileBrowserSymbol.getVariableName());
            assertTrue(fileBrowserSymbol.getDoubleClick());
            assertFalse(fileBrowserSymbol.isReadOnly());
            symbol = table.getSymbol("symbol1");
            assertTrue(symbol instanceof FileBrowserSymbol);
            fileBrowserSymbol = (FileBrowserSymbol) symbol;
            assertEquals("pagename", fileBrowserSymbol.getPage());
            assertEquals("symbol1", fileBrowserSymbol.getSymbolName());
            assertEquals("symbol1", fileBrowserSymbol.getFileBrowserName());
            assertEquals("path1", fileBrowserSymbol.getVariableName());
            assertFalse(fileBrowserSymbol.getDoubleClick());
            assertFalse(fileBrowserSymbol.isReadOnly());
            symbol = table.getSymbol("symbol2" + FileBrowserSymbol.DOUBLECLICK);
            assertTrue(symbol instanceof FileBrowserSymbol);
            fileBrowserSymbol = (FileBrowserSymbol) symbol;
            assertEquals("pagename", fileBrowserSymbol.getPage());
            assertEquals("symbol2" + FileBrowserSymbol.DOUBLECLICK, fileBrowserSymbol.getSymbolName());
            assertEquals("symbol2", fileBrowserSymbol.getFileBrowserName());
            assertEquals("path2", fileBrowserSymbol.getVariableName());
            assertTrue(fileBrowserSymbol.getDoubleClick());
            assertFalse(fileBrowserSymbol.isReadOnly());
            symbol = table.getSymbol("symbol2");
            assertTrue(symbol instanceof FileBrowserSymbol);
            fileBrowserSymbol = (FileBrowserSymbol) symbol;
            assertEquals("pagename", fileBrowserSymbol.getPage());
            assertEquals("symbol2", fileBrowserSymbol.getSymbolName());
            assertEquals("symbol2", fileBrowserSymbol.getFileBrowserName());
            assertEquals("path2", fileBrowserSymbol.getVariableName());
            assertFalse(fileBrowserSymbol.getDoubleClick());
            assertFalse(fileBrowserSymbol.isReadOnly());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}
