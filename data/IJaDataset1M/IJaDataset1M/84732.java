package com.googlecode.quillen.application;

import com.googlecode.quillen.domain.AttributeStorageException;
import com.googlecode.quillen.domain.ObjectStorageException;
import com.googlecode.quillen.domain.Snapshot;
import com.googlecode.quillen.domain.FileInfo;
import com.googlecode.quillen.util.WorkQueueAbortedException;
import com.googlecode.quillen.util.ResultConsumer;
import java.io.IOException;
import java.util.Date;
import java.util.Collection;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: greg
 * Date: Dec 18, 2008
 * Time: 2:21:28 AM
 */
public interface Admin {

    void install() throws AttributeStorageException, ObjectStorageException, WorkQueueAbortedException;

    void uninstall() throws AttributeStorageException, ObjectStorageException, WorkQueueAbortedException;

    void listSnapshots(ResultConsumer<Snapshot> consumer) throws AttributeStorageException, ParseException;

    void listSnapshots(String prefix, ResultConsumer<Snapshot> consumer) throws AttributeStorageException, ParseException;

    void listSnapshots(String prefix, Date maxDate, ResultConsumer<Snapshot> consumer) throws AttributeStorageException, ParseException;

    void listSnapshotContents(Collection<String> snapshots, String filenamePrefix, ResultConsumer<FileInfo> consumer) throws AttributeStorageException, ParseException;

    void deleteSnapshots(Collection<String> snapshots, ResultConsumer<FileInfo> consumer) throws AttributeStorageException, ObjectStorageException, IOException, WorkQueueAbortedException, ParseException;

    void deleteSnapshots(String prefix, ResultConsumer<FileInfo> consumer) throws AttributeStorageException, IOException, ObjectStorageException, WorkQueueAbortedException, ParseException;

    void deleteSnapshots(String prefix, Date maxDate, ResultConsumer<FileInfo> consumer) throws AttributeStorageException, IOException, ObjectStorageException, WorkQueueAbortedException, ParseException;
}
