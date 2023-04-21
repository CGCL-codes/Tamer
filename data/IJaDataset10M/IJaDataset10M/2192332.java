package org.cishell.utilities;

import java.io.File;
import java.util.Dictionary;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class AlgorithmUtilities {

    public static AlgorithmFactory getAlgorithmFactoryByFilter(String filter, BundleContext bundleContext) throws AlgorithmNotFoundException {
        ServiceReference[] algorithmFactoryReferences;
        try {
            algorithmFactoryReferences = bundleContext.getServiceReferences(AlgorithmFactory.class.getName(), filter);
        } catch (InvalidSyntaxException invalidSyntaxException) {
            throw new AlgorithmNotFoundException(invalidSyntaxException);
        }
        if (algorithmFactoryReferences != null && algorithmFactoryReferences.length != 0) {
            ServiceReference algorithmFactoryReference = algorithmFactoryReferences[0];
            AlgorithmFactory algorithmFactory = (AlgorithmFactory) bundleContext.getService(algorithmFactoryReference);
            return algorithmFactory;
        } else {
            throw new AlgorithmNotFoundException("Unable to find an " + "algorithm that satisfied the following filter:\n" + filter);
        }
    }

    public static AlgorithmFactory getAlgorithmFactoryByPID(String pid, BundleContext bundleContext) throws AlgorithmNotFoundException {
        String filter = "(service.pid=" + pid + ")";
        return getAlgorithmFactoryByFilter(filter, bundleContext);
    }

    public static Data[] cloneSingletonData(Data[] data) {
        return new Data[] { new BasicData(data[0].getMetadata(), data[0].getData(), data[0].getFormat()) };
    }

    @SuppressWarnings("unchecked")
    public static String guessSourceDataFilename(Data data) {
        if (data == null) {
            return "";
        }
        Dictionary metadata = data.getMetadata();
        String label = (String) metadata.get(DataProperty.LABEL);
        Data parent = (Data) metadata.get(DataProperty.PARENT);
        if (label != null && label.indexOf(File.separator) != -1) {
            String escapedFileSeparator = File.separator;
            if ("\\".equals(escapedFileSeparator)) {
                escapedFileSeparator = "\\\\";
            }
            String[] pathTokens = label.split(escapedFileSeparator);
            String guessedFilename = pathTokens[pathTokens.length - 1];
            int lastExtensionSeparatorIndex = guessedFilename.lastIndexOf(".");
            if (lastExtensionSeparatorIndex != -1) {
                String guessedNameProper = guessedFilename.substring(0, lastExtensionSeparatorIndex);
                String guessedExtension = guessedFilename.substring(lastExtensionSeparatorIndex);
                String[] extensionTokens = guessedExtension.split("\\s+");
                return guessedNameProper + extensionTokens[0];
            } else {
                return guessSourceDataFilename(parent);
            }
        } else {
            return guessSourceDataFilename(parent);
        }
    }

    @SuppressWarnings("unchecked")
    public static Data[] executeAlgorithm(AlgorithmFactory algorithmFactory, ProgressMonitor progressMonitor, Data[] data, Dictionary parameters, CIShellContext ciShellContext) throws AlgorithmExecutionException {
        Algorithm algorithm = algorithmFactory.createAlgorithm(data, parameters, ciShellContext);
        if ((progressMonitor != null) && (algorithm instanceof ProgressTrackable)) {
            ProgressTrackable progressTrackable = (ProgressTrackable) algorithm;
            progressTrackable.setProgressMonitor(progressMonitor);
        }
        Data[] result = algorithm.execute();
        return result;
    }

    public static Data[] executeAlgorithm(AlgorithmFactory algorithmFactory, Data[] data, Dictionary parameters, CIShellContext ciShellContext) throws AlgorithmExecutionException {
        return executeAlgorithm(algorithmFactory, null, data, parameters, ciShellContext);
    }
}
