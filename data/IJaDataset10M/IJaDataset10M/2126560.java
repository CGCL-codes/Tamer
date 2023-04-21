package openfarm.multithreading.machine;

import openfarm.jni.template.Template;

public interface IJobThread extends Runnable {

    public Job getJob();

    public Template getTemplate();
}
