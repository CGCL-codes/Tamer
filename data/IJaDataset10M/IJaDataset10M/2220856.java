package org.tolk.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.jmock.Expectations;
import org.tolk.BaseTestCase;

/**
 * 
 * @author Johan Roets
 */
public class DataSourceTest extends BaseTestCase {

    private DataSourceExt dataSource;

    private BufferedReader inputStreamMock;

    private PrintWriter outputStreamMock;

    @Override
    public void setUp() {
        this.dataSource = new DataSourceExt();
        this.inputStreamMock = mock(BufferedReader.class);
        this.outputStreamMock = mock(PrintWriter.class);
        this.dataSource.setInputStream(this.inputStreamMock);
        this.dataSource.setOutputStream(this.outputStreamMock);
    }

    public void testReadSuccesful() throws IOException {
        checking(new Expectations() {

            {
                one(DataSourceTest.this.inputStreamMock).readLine();
                will(returnValue(""));
            }
        });
        String result = this.dataSource.read();
        assertNotNull(result);
    }

    public void testReadNull() throws IOException {
        this.dataSource.setInputStream(null);
        checking(new Expectations() {

            {
            }
        });
        String result = this.dataSource.read();
        assertNull(result);
    }

    public void testWrite() {
        checking(new Expectations() {

            {
                one(DataSourceTest.this.outputStreamMock).println("somestring");
                one(DataSourceTest.this.outputStreamMock).flush();
            }
        });
        this.dataSource.write("somestring");
    }

    public void testNullWrite() {
        checking(new Expectations() {

            {
            }
        });
        this.dataSource.setOutputStream(null);
        this.dataSource.write("somestring");
    }

    class DataSourceExt extends DataSource {
    }
}
