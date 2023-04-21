package org.lastfm.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.asmatron.messengine.ControlEngine;
import org.asmatron.messengine.engines.support.ControlEngineConfigurator;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Before;
import org.junit.Test;
import org.lastfm.exception.InvalidId3VersionException;
import org.lastfm.metadata.Metadata;
import org.lastfm.util.FileUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestMetadataExtractor {

    @InjectMocks
    private MetadataExtractor metadataExtractor = new MetadataExtractor();

    @Mock
    private FileUtils fileUtils;

    @Mock
    private File root;

    @Mock
    private ControlEngineConfigurator configurator;

    @Mock
    private ControlEngine controlEngine;

    private List<File> fileList;

    private static final int FIRST_ELEMENT = 0;

    private File pepeGarden = new File("src/test/resources/audio/Jaytech - Pepe Garden (Original Mix).mp3");

    private File signalRunners = new File("src/test/resources/audio/Signalrunners And Julie Thompson - These Shoulders (Andy Moor Remix).mp3");

    private File checkStyleFile = new File("src/test/resources/checkstyle/checkstyle.xml");

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(configurator.getControlEngine()).thenReturn(controlEngine);
        when(fileUtils.isMp3File(pepeGarden)).thenReturn(true);
        fileList = new ArrayList<File>();
    }

    @Test
    public void shouldExtractMetadata() throws Exception {
        setFileListExpectations();
        List<Metadata> metadatas = metadataExtractor.extractMetadata(root);
        Metadata metadata = metadatas.get(FIRST_ELEMENT);
        verifyExpectations(metadatas, metadata);
    }

    @Test
    public void shouldDetectANotValidAudioFile() throws Exception {
        fileList.add(pepeGarden);
        fileList.add(checkStyleFile);
        when(fileUtils.isMp3File(checkStyleFile)).thenReturn(false);
        when(fileUtils.getFileList(root)).thenReturn(fileList);
        List<Metadata> metadatas = metadataExtractor.extractMetadata(root);
        Metadata metadata = metadatas.get(FIRST_ELEMENT);
        verifyExpectations(metadatas, metadata);
    }

    private void verifyExpectations(List<Metadata> metadatas, Metadata metadata) throws InterruptedException, IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException, InvalidId3VersionException {
        assertEquals(1, metadatas.size());
        verify(fileUtils).getFileList(root);
        assertEquals("Jaytech", metadata.getArtist());
    }

    @Test
    public void shouldCleanMetadataList() throws Exception {
        setFileListExpectations();
        List<Metadata> metadatas = metadataExtractor.extractMetadata(root);
        assertEquals(1, metadatas.size());
        metadatas = metadataExtractor.extractMetadata(root);
        assertEquals(1, metadatas.size());
    }

    private void setFileListExpectations() throws InterruptedException, IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException, InvalidId3VersionException {
        fileList.add(pepeGarden);
        when(fileUtils.getFileList(root)).thenReturn(fileList);
    }

    @Test
    public void shouldDetectAFileWithoutMinimumMetadata() throws Exception {
        when(fileUtils.isMp3File(signalRunners)).thenReturn(true);
        fileList.add(signalRunners);
        when(fileUtils.getFileList(root)).thenReturn(fileList);
        List<Metadata> metadatas = metadataExtractor.extractMetadata(root);
        assertTrue(metadatas.isEmpty());
        verify(fileUtils, never()).isM4aFile(signalRunners);
    }
}
