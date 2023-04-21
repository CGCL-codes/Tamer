package org.xmlvm.proc.out;

import java.util.HashMap;
import java.util.Map;
import org.xmlvm.Log;
import org.xmlvm.main.Arguments;
import org.xmlvm.proc.BundlePhase1;
import org.xmlvm.proc.BundlePhase2;
import org.xmlvm.proc.XmlvmProcessImpl;
import org.xmlvm.proc.XmlvmResource;
import org.xmlvm.proc.XsltRunner;

/**
 * This process takes XMLVM and turns it into Java source code.
 */
public class JavaOutputProcess extends XmlvmProcessImpl {

    private class JavaTranslationThread extends Thread {

        private final XmlvmResource[] allResources;

        private final int start;

        private final int end;

        private final BundlePhase2 resources;

        public JavaTranslationThread(XmlvmResource[] allResources, int start, int end, BundlePhase2 resources) {
            this.allResources = allResources;
            this.start = start;
            this.end = end;
            this.resources = resources;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; ++i) {
                XmlvmResource resource = allResources[i];
                if (resource == null) {
                    continue;
                }
                String resourceName = resource.getName();
                Log.debug("JavaOutputProcess: Processing " + resourceName);
                OutputFile file = generateJava(resource);
                file.setLocation(arguments.option_out());
                String packageName = resource.getPackageName().replace('.', '/');
                String fileName = resourceName + JAVA_EXTENSION;
                if (!packageName.isEmpty()) {
                    fileName = packageName + '/' + fileName;
                }
                file.setFileName(fileName);
                resources.addOutputFile(file);
            }
        }
    }

    private static final String JAVA_EXTENSION = ".java";

    private static final String TAG = JavaOutputProcess.class.getSimpleName();

    public JavaOutputProcess(Arguments arguments) {
        super(arguments);
        addSupportedInput(RecursiveResourceLoadingProcess.class);
    }

    @Override
    public boolean processPhase1(BundlePhase1 bundle) {
        return true;
    }

    @Override
    public boolean processPhase2(BundlePhase2 bundle) {
        Map<String, XmlvmResource> mappedResources = new HashMap<String, XmlvmResource>();
        for (XmlvmResource resource : bundle.getResources()) {
            if (resource != null) {
                mappedResources.put(resource.getFullName(), resource);
            }
        }
        long startTime = System.currentTimeMillis();
        XmlvmResource[] allResources = mappedResources.values().toArray(new XmlvmResource[0]);
        int threadCount = Runtime.getRuntime().availableProcessors();
        int itemsPerThread = (int) Math.ceil(allResources.length / (float) threadCount);
        Log.debug(TAG, "Threads: " + threadCount);
        Log.debug(TAG, "Items per thread: " + itemsPerThread);
        JavaTranslationThread[] threads = new JavaTranslationThread[threadCount];
        for (int i = 0; i < threadCount; ++i) {
            int start = i * itemsPerThread;
            int end = Math.min(start + itemsPerThread - 1, allResources.length - 1);
            threads[i] = new JavaTranslationThread(allResources, start, end, bundle);
            threads[i].start();
        }
        for (int i = 0; i < threadCount; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        long endTime = System.currentTimeMillis();
        Log.debug(TAG, "Java Processing took: " + (endTime - startTime) + " ms.");
        return true;
    }

    protected OutputFile generateJava(XmlvmResource xmlvm) {
        return XsltRunner.runXSLT("xmlvm2java.xsl", xmlvm.getXmlvmDocument());
    }
}
