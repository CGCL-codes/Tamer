package de.ufinke.cubaja.sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

final class SortTask implements Runnable {

    private final SortManager manager;

    private boolean loop;

    private boolean fileTaskStarted;

    private List<SortArray> arrayList;

    public SortTask(SortManager manager) {
        this.manager = manager;
        arrayList = new ArrayList<SortArray>(manager.getArrayCount());
    }

    public void run() {
        try {
            work();
        } catch (Throwable t) {
            manager.setError(t);
        }
    }

    private void work() throws Exception {
        final BlockingQueue<Request> queue = manager.getSortQueue();
        loop = true;
        while (loop) {
            final Request request = queue.poll(1, TimeUnit.SECONDS);
            if (manager.hasError()) {
                loop = false;
            } else if (request != null) {
                handleRequest(request);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleRequest(final Request request) throws Exception {
        switch(request.getType()) {
            case SORT_ARRAY:
                sortArray((SortArray) request.getData());
                break;
            case SWITCH_STATE:
                if (fileTaskStarted) {
                    mergeFromFile();
                } else {
                    mergeFromArrayList();
                }
                break;
            case INIT_RUN_MERGE:
                initRunMerge((List<Run>) request.getData());
                break;
            case CLOSE:
                close();
                break;
        }
    }

    private void mergeFromFile() throws Exception {
        drainToFile();
        writeQueue(manager.getFileQueue(), new Request(RequestType.SWITCH_STATE));
    }

    private void initRunMerge(List<Run> runList) throws Exception {
        for (Run run : runList) {
            run.requestNextBlock();
        }
        mergeResult(runList);
    }

    private void mergeFromArrayList() throws Exception {
        mergeResult(arrayList);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void mergeResult(List sources) throws Exception {
        final Merger merger = new Merger(manager.getComparator(), sources);
        final BlockingQueue<Request> queue = manager.getMainQueue();
        mergeToQueue(merger, queue, RequestType.RESULT);
        writeQueue(queue, new Request(RequestType.END_OF_DATA));
    }

    private void sortArray(final SortArray sortArray) throws Exception {
        manager.getAlgorithm().sort(sortArray.getArray(), sortArray.getSize(), manager.getComparator());
        arrayList.add(sortArray);
        if (arrayList.size() == manager.getArrayCount()) {
            drainToFile();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void drainToFile() throws Exception {
        if (arrayList.size() == 0) {
            return;
        }
        if (!fileTaskStarted) {
            manager.submit(new FileTask(manager));
            fileTaskStarted = true;
        }
        final Merger merger = new Merger(manager.getComparator(), arrayList);
        final BlockingQueue<Request> queue = manager.getFileQueue();
        writeQueue(queue, new Request(RequestType.BEGIN_RUN));
        mergeToQueue(merger, queue, RequestType.WRITE_BLOCKS);
        writeQueue(queue, new Request(RequestType.END_RUN));
        arrayList.clear();
    }

    @SuppressWarnings("rawtypes")
    private void mergeToQueue(final Merger merger, final BlockingQueue<Request> queue, final RequestType type) throws Exception {
        final int queueSize = manager.getArraySize();
        final Iterator iterator = merger.iterator();
        Object[] array = new Object[queueSize];
        int size = 0;
        while (iterator.hasNext() && loop) {
            if (size == array.length) {
                writeQueue(queue, new Request(type, new SortArray(array, size)));
                array = new Object[queueSize];
                size = 0;
            }
            array[size++] = iterator.next();
        }
        if (size > 0) {
            writeQueue(queue, new Request(type, new SortArray(array, size)));
        }
        arrayList.clear();
    }

    private void close() throws Exception {
        if (fileTaskStarted) {
            writeQueue(manager.getFileQueue(), new Request(RequestType.CLOSE));
        }
        loop = false;
    }

    private void writeQueue(final BlockingQueue<Request> queue, final Request request) throws Exception {
        boolean written = false;
        while ((!written) && loop) {
            written = queue.offer(request, 1, TimeUnit.SECONDS);
            if (manager.hasError()) {
                loop = false;
            }
        }
    }
}
