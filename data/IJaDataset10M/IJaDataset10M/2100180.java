package fitlibrary.flow;

import static fitlibrary.matcher.TableBuilderForTests.cell;
import static fitlibrary.matcher.TableBuilderForTests.row;
import static fitlibrary.matcher.TableBuilderForTests.table;
import static fitlibrary.matcher.TableBuilderForTests.tables;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import fit.ColumnFixture;
import fit.Fixture;
import fit.Parse;
import fit.exception.FitParseException;
import fitlibrary.table.Table;
import fitlibrary.table.Tables;

@RunWith(JMock.class)
public class TestDoFlowWithFixture {

    final Mockery context = new Mockery();

    final DoFlowDriverForTests doFlowDriver = new DoFlowDriverForTests(context);

    final Tables tables = tables().with(table().with(row().with(cell(), cell()), row().with(cell(), cell()))).mock(context);

    @Before
    public void allows() {
        context.checking(new Expectations() {

            {
                allowing(tables.at(0)).fromAt(0);
                will(returnValue(tables.at(0)));
            }
        });
    }

    @Test
    public void runWithFixture() throws FitParseException {
        final MockFixture mockFixture = context.mock(MockFixture.class);
        final Fixture evaluator = new ColumnFixture() {

            @Override
            public void doTable(Parse parse) {
                mockFixture.doTable(parse);
            }
        };
        Table table0 = tables.at(0);
        doFlowDriver.startingOnTable(table0);
        doFlowDriver.interpretingRowReturning(table0.at(0), evaluator);
        doFlowDriver.interpretingFixture(mockFixture, table0);
        doFlowDriver.poppingScopeStackAtEndOfLastTableGiving();
        doFlowDriver.finishingTable(table0);
        doFlowDriver.runStorytest(tables);
    }

    static interface MockFixture {

        void doTable(Parse parse);
    }
}
