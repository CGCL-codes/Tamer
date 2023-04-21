package net.ikenna.yadet.core;

import net.ikenna.yadet.core.data.*;
import net.ikenna.yadet.core.rowprocessor.*;
import org.junit.*;
import static org.mockito.Mockito.*;

public class DataManipulatorTest {

    private RowProcessor rowProcessor;

    private RowSet rawRowSet;

    private RowSet processedRowSet;

    private Inserter inserter;

    private Extractor extractor;

    @Before
    public void setUp() {
        rowProcessor = mock(RowProcessor.class);
        rawRowSet = mock(RowSet.class);
        processedRowSet = mock(RowSet.class);
        inserter = mock(Inserter.class);
        extractor = mock(Extractor.class);
    }

    @Test
    public void shouldExtractDataFromDatabase() {
        DataManipulator manipulator = new DataManipulator(extractor, rowProcessor, inserter);
        manipulator.run();
        verify(extractor).extract();
    }

    @Test
    public void shouldProcessData() {
        when(extractor.extract()).thenReturn(rawRowSet);
        new DataManipulator(extractor, rowProcessor, inserter).run();
        verify(rowProcessor).process(rawRowSet);
    }

    @Test
    public void shouldInsertProcessedData() {
        when(rowProcessor.process(any(RowSet.class))).thenReturn(processedRowSet);
        new DataManipulator(extractor, rowProcessor, inserter).run();
        verify(inserter).insertIntoNewTable(processedRowSet);
    }
}
