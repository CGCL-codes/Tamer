package fitlibrary.table;

import static fitlibrary.matcher.TableBuilderForTests.cell;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import fitlibrary.dynamicVariable.VariableResolver;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.exception.FitLibraryShowException;
import fitlibrary.exception.FitLibraryShowException.Show;
import fitlibrary.runResults.TestResults;

@RunWith(JMock.class)
public class TestRowOnListWithTestResults {

    Mockery context = new Mockery();

    TestResults testResults = context.mock(TestResults.class);

    VariableResolver resolver = context.mock(VariableResolver.class);

    Cell cell0 = cell("0").mock(context, "", 0);

    Cell cell1 = cell("1").mock(context, "", 1);

    Cell cell2 = cell("2").mock(context, "", 2);

    Row row12 = row(cell0, cell1);

    Row row123 = row(cell0, cell1, cell2);

    @Before
    public void useListsFactory() {
        TableFactory.useOnLists(true);
    }

    @After
    public void stopUsingListsFactory() {
        TableFactory.pop();
    }

    @Test
    public void text() {
        assertThat(row12.text(1, resolver), is("1"));
    }

    @Test
    public void pass() {
        context.checking(new Expectations() {

            {
                oneOf(testResults).pass();
            }
        });
        row12.pass(testResults);
        assertThat(row12.didPass(), is(true));
    }

    @Test
    public void fail() {
        context.checking(new Expectations() {

            {
                oneOf(testResults).fail();
            }
        });
        row12.fail(testResults);
        assertThat(row12.didFail(), is(true));
    }

    @Test
    public void error() {
        final FitLibraryException e = new FitLibraryException("aa");
        context.checking(new Expectations() {

            {
                oneOf(cell0).error(testResults, e);
            }
        });
        row12.error(testResults, e);
    }

    @Test
    public void handleShow() {
        row12.error(testResults, new FitLibraryShowException(new Show("SHoW")));
        assertThat(row12.size(), is(3));
        assertThat(row12.at(2).text(), is("SHoW"));
    }

    @Test
    public void missing() {
        context.checking(new Expectations() {

            {
                oneOf(cell0).expectedElementMissing(testResults);
            }
        });
        row12.missing(testResults);
    }

    @Test
    public void ignore() {
        context.checking(new Expectations() {

            {
                oneOf(cell0).ignore(testResults);
                oneOf(cell1).ignore(testResults);
            }
        });
        row12.ignore(testResults);
    }

    @Test
    public void passKeywords() {
        context.checking(new Expectations() {

            {
                oneOf(cell0).pass(testResults);
                oneOf(cell2).pass(testResults);
            }
        });
        row123.passKeywords(testResults);
    }

    @Test
    public void deepCopy() {
        final Cell cell1copy = cell().mock(context, "", 55);
        final Cell cell2copy = cell().mock(context, "", 66);
        context.checking(new Expectations() {

            {
                oneOf(cell0).deepCopy();
                will(returnValue(cell1copy));
                oneOf(cell1).deepCopy();
                will(returnValue(cell2copy));
            }
        });
        row12.setLeader("LL");
        row12.setTrailer("TT");
        Row deepCopy = row12.deepCopy();
        assertThat(deepCopy.size(), is(2));
        assertThat(deepCopy.at(0), is(cell1copy));
        assertThat(deepCopy.at(1), is(cell2copy));
        assertThat(deepCopy.getLeader(), is("LL"));
        assertThat(deepCopy.getTrailer(), is("TT"));
    }

    @Test
    public void toHtml() {
        final StringBuilder stringBuilder = new StringBuilder();
        context.checking(new Expectations() {

            {
                oneOf(cell0).toHtml(stringBuilder);
                oneOf(cell1).toHtml(stringBuilder);
            }
        });
        row12.setLeader("LL");
        row12.setTrailer("TT");
        row12.toHtml(stringBuilder);
        assertThat(stringBuilder.toString(), is("LL<tr></tr>TT"));
    }

    protected static Row row(Cell... cells) {
        Row row = new RowOnList();
        for (Cell cell : cells) row.add(cell);
        return row;
    }
}
