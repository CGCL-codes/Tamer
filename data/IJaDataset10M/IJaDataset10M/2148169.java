package jumpingnotes.codec;

public interface CodecTaskListener {

    void onTaskStart(CodecTask task);

    void onTaskDone(CodecTask task);

    void onTaskProgress(CodecTask task, int percentage);
}
