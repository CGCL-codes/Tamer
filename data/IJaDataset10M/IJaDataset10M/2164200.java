package tests.taskgraph.tasks.io;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import taskgraph.channels.Channel;
import taskgraph.ports.InputPort;
import taskgraph.ports.OutputPort;
import taskgraph.tasks.io.JDBCInsert;
import taskgraph.tasks.io.JDBCResultSet;

public class JDBCInsertTest {

    /**
	 * Runs the test suite in this class from the command line.
	 * 
	 * @param args	Arguments are ignored.
	 */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.runClasses(JDBCInsertTest.class);
    }

    /**
	 * Provides compatibility with 3.x versions of JUnit.
	 * 
	 * @return A 3.x-compatible test suite.
	 */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JDBCInsertTest.class);
    }

    private static final String DATABASE_URL = "jdbc:mysql://localhost/books";

    private InputPort<BookTitle> input;

    @Before
    public void setup() {
        Channel<BookTitle> channel = new Channel<BookTitle>();
        input = channel.getInputPort();
    }

    @After
    public void cleanup() {
        input.close();
    }

    @Test
    public void defaultConstructor() {
        JDBCInsert<BookTitle> task = new JDBCInsert<BookTitle>();
        Assert.assertNotNull(task);
    }

    @Test
    public void defaultInstanceClass() {
        JDBCInsert<BookTitle> task = new JDBCInsert<BookTitle>();
        Assert.assertEquals(JDBCInsert.class, task.getClass());
    }

    @Test
    public void constructorWithArgs() {
        JDBCInsert<BookTitle> task = new JDBCInsert<BookTitle>(input, "url");
        Assert.assertNotNull(task);
    }

    @Test
    public void instanceClassWithArgs() {
        JDBCInsert<BookTitle> task = new JDBCInsert<BookTitle>(input, "url");
        Assert.assertEquals(JDBCInsert.class, task.getClass());
    }

    @Test
    public void constructorWithFullArgs() {
        JDBCInsert<BookTitle> task = new JDBCInsert<BookTitle>(input, "url", "user", "pwd");
        Assert.assertNotNull(task);
    }

    @Test
    public void instanceClassWithFullArgs() {
        JDBCInsert<BookTitle> task = new JDBCInsert<BookTitle>(input, "url", "user", "pwd");
        Assert.assertEquals(JDBCInsert.class, task.getClass());
    }

    @Test
    public void testRunWithOneBookTitle() throws IOException, ParseException, SQLException {
        BookTitle expected = new BookTitle("0131855005", "C++ How to Program", 5, "2005");
        Channel<BookTitle> ch = new Channel<BookTitle>();
        JDBCInsert<BookTitle> writeTask = new JDBCInsert<BookTitle>(ch.getInputPort(), DATABASE_URL, "dataflow", "dataflow");
        OutputPort<BookTitle> output = ch.getOutputPort();
        output.write(expected);
        output.close();
        writeTask.run();
        ch = new Channel<BookTitle>();
        JDBCResultSet<BookTitle> readTask = new JDBCResultSet<BookTitle>(ch.getOutputPort(), BookTitle.class, DATABASE_URL, "dataflow", "dataflow", "SELECT ISBN, Title, EditionNumber, Copyright " + "FROM Titles " + "WHERE ISBN = '0131855005'");
        readTask.run();
        InputPort<BookTitle> input = ch.getInputPort();
        BookTitle actual = input.read();
        input.close();
        Assert.assertEquals(expected.getISBN(), actual.getISBN());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getEditionNumber(), actual.getEditionNumber());
        Assert.assertEquals(expected.getCopyright(), actual.getCopyright());
        Connection connection = DriverManager.getConnection(DATABASE_URL, "dataflow", "dataflow");
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM Titles WHERE ISBN = '0131855005'");
    }

    @Test
    public void testRunWithBookTitles() throws IOException, ParseException, SQLException {
        BookTitle[] titles = { new BookTitle("9991450913", "Internet & World Wide Web How to Program", 3, "2004"), new BookTitle("9992525239", "Visual C# 2005 How to Program", 2, "2006"), new BookTitle("9993828274", "Operating Systems", 3, "2004"), new BookTitle("9994857576", "C++ How to Program", 5, "2005"), new BookTitle("9995869000", "Visual Basic 2005 How to Program", 3, "2006"), new BookTitle("9996222205", "Java How to Program", 7, "2007"), new BookTitle("9997404168", "C How to Program", 5, "2007") };
        Channel<BookTitle> ch = new Channel<BookTitle>();
        JDBCInsert<BookTitle> writeTask = new JDBCInsert<BookTitle>(ch.getInputPort(), DATABASE_URL, "dataflow", "dataflow");
        OutputPort<BookTitle> output = ch.getOutputPort();
        for (BookTitle title : titles) output.write(title);
        output.close();
        writeTask.run();
        ch = new Channel<BookTitle>();
        JDBCResultSet<BookTitle> readTask = new JDBCResultSet<BookTitle>(ch.getOutputPort(), BookTitle.class, DATABASE_URL, "dataflow", "dataflow", "SELECT ISBN, Title, EditionNumber, Copyright " + "FROM Titles " + "WHERE ISBN > '999' " + "ORDER BY ISBN ASC");
        readTask.run();
        InputPort<BookTitle> input = ch.getInputPort();
        for (BookTitle expected : titles) {
            BookTitle actual = input.read();
            Assert.assertEquals(expected.getISBN(), actual.getISBN());
            Assert.assertEquals(expected.getTitle(), actual.getTitle());
            Assert.assertEquals(expected.getEditionNumber(), actual.getEditionNumber());
            Assert.assertEquals(expected.getCopyright(), actual.getCopyright());
        }
        input.close();
        Connection connection = DriverManager.getConnection(DATABASE_URL, "dataflow", "dataflow");
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM Titles WHERE ISBN > '999'");
    }

    @Test
    public void testRunWithBookData() throws IOException, ParseException, SQLException {
        BookData[] titles = { new BookData("9991450913", "Internet & World Wide Web How to Program", 3, "2004"), new BookData("9992125239", "Visual C# 2005 How to Program", 2, "2006"), new BookData("9993828274", "Operating Systems", 3, "2004"), new BookData("9994857576", "C++ How to Program", 5, "2005"), new BookData("9995869000", "Visual Basic 2005 How to Program", 3, "2006"), new BookData("9996222205", "Java How to Program", 7, "2007"), new BookData("9997404168", "C How to Program", 5, "2007") };
        Channel<BookData> ch = new Channel<BookData>();
        JDBCInsert<BookData> writeTask = new JDBCInsert<BookData>(ch.getInputPort(), DATABASE_URL, "dataflow", "dataflow");
        OutputPort<BookData> output = ch.getOutputPort();
        for (BookData title : titles) output.write(title);
        output.close();
        writeTask.run();
        ch = new Channel<BookData>();
        JDBCResultSet<BookData> readTask = new JDBCResultSet<BookData>(ch.getOutputPort(), BookData.class, DATABASE_URL, "dataflow", "dataflow", "SELECT ISBN, Title, EditionNumber, Copyright " + "FROM Titles " + "WHERE ISBN > '999' " + "ORDER BY ISBN ASC");
        readTask.run();
        InputPort<BookData> input = ch.getInputPort();
        for (BookData expected : titles) {
            BookData actual = input.read();
            Assert.assertEquals(expected.getKey(), actual.getKey());
            Assert.assertEquals(expected.getName(), actual.getName());
            Assert.assertEquals(expected.getEdition(), actual.getEdition());
            Assert.assertEquals(expected.getNotice(), actual.getNotice());
        }
        input.close();
        Connection connection = DriverManager.getConnection(DATABASE_URL, "dataflow", "dataflow");
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM Titles WHERE ISBN > '999'");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNullInputPort() throws IOException {
        new JDBCInsert<BookTitle>(null, "url");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNullURL() throws IOException {
        new JDBCInsert<BookTitle>(input, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor2WithNullInputPort() throws IOException {
        new JDBCInsert<BookTitle>(null, "url", "user", "pwd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor2WithNullURL() throws IOException {
        new JDBCInsert<BookTitle>(input, null, "user", "pwd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor2WithNullUser() throws IOException {
        new JDBCInsert<BookTitle>(input, "url", null, "pwd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor2WithNullPwd() throws IOException {
        new JDBCInsert<BookTitle>(input, "url", "user", null);
    }
}
