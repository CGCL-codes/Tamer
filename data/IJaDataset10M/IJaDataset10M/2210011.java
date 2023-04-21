package uk.co.brunella.osgi.bdt.fit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import uk.co.brunella.osgi.bdt.bundle.BundleDescriptor;
import uk.co.brunella.osgi.bdt.bundle.BundleRepository;
import uk.co.brunella.osgi.bdt.bundle.VersionRange;
import uk.co.brunella.osgi.bdt.junit.annotation.OSGiBDTTest;
import uk.co.brunella.osgi.bdt.junit.runner.OSGiBDTJUnitRunner;
import uk.co.brunella.osgi.bdt.junit.runner.model.OSGiBDTTestWrapper;
import uk.co.brunella.osgi.bdt.repository.BundleRepositoryPersister;
import uk.co.brunella.osgi.bdt.repository.Deployer;
import fit.Parse;
import fitnesse.fixtures.TableFixture;

public class AbstractOSGiBDTTableFixture extends TableFixture {

    protected Class<?> testClass;

    protected String testBundle;

    private BundleRepository repository;

    public AbstractOSGiBDTTableFixture(Class<?> testClass, String testBundle) {
        this.testClass = testClass;
        this.testBundle = testBundle;
    }

    @Override
    protected void doStaticTable(int rows) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        String[][] data = new String[rows][];
        parameters.put("data", data);
        for (int row = 0; row < rows; row++) {
            List<String> rowData = new ArrayList<String>();
            Parse newCell = null;
            Parse oldCell = null;
            for (int col = 0; col < 5; col++) {
                newCell = getCell(row, col);
                if (newCell != oldCell) {
                    oldCell = newCell;
                    rowData.add(newCell.text());
                }
            }
            data[row] = (String[]) rowData.toArray(new String[rowData.size()]);
        }
        data = data.clone();
        for (int row = 0; row < data.length; row++) {
            data[row] = data[row].clone();
        }
        executeTestCases(parameters);
        if (parameters.get("results") instanceof String[][]) {
            String[][] results = (String[][]) parameters.get("results");
            for (int row = 0; row < data.length; row++) {
                for (int col = 0; col < data[row].length; col++) {
                    if (data[row][col].equals(results[row][col])) {
                        right(row, col);
                    } else {
                        wrong(row, col, results[row][col]);
                    }
                }
            }
        }
    }

    private void executeTestCases(Map<String, Object> parameters) {
        OSGiBDTTest annotation = testClass.getAnnotation(OSGiBDTTest.class);
        OSGiBDTTestWrapper testArguments = null;
        try {
            if (args.length > 0) {
                loadRepository(args[0]);
                testArguments = new OSGiBDTTestWrapper(annotation, new String[] { args[0] });
            } else {
                loadRepository(annotation.repositories()[0]);
                testArguments = new OSGiBDTTestWrapper(annotation);
            }
        } catch (IOException e) {
            exception(getCell(0, 0), new RuntimeException("Repository cannot be loaded: " + e.getMessage()));
            return;
        }
        try {
            OSGiBDTJUnitRunner runner = new OSGiBDTJUnitRunner(testClass, testArguments, findBundle(testBundle), parameters);
            RunNotifier notifier = new RunNotifier();
            OSGiBDTFitRunListener listener = new OSGiBDTFitRunListener();
            notifier.addListener(listener);
            runner.run(notifier);
        } catch (InitializationError e) {
            exception(getCell(0, 0), e);
        }
    }

    private void loadRepository(String repositoryLocation) throws IOException {
        BundleRepositoryPersister persister = new BundleRepositoryPersister(new File(repositoryLocation));
        repository = persister.load();
    }

    private File findBundle(String bundleName) {
        String name;
        VersionRange versionRange;
        if (bundleName.contains(";version=")) {
            name = bundleName.substring(0, bundleName.indexOf(';'));
            versionRange = VersionRange.parseVersionRange(bundleName.substring(bundleName.indexOf(';') + ";version=".length()));
        } else {
            name = bundleName;
            versionRange = VersionRange.parseVersionRange("");
        }
        BundleDescriptor[] descriptors = repository.resolveBundle(name, versionRange, true);
        if (descriptors.length > 0) {
            BundleDescriptor descriptor = descriptors[0];
            File bundleJarFile = new File(repository.getLocation(), Deployer.BUNDLES_DIRECTORY + File.separator + descriptor.getBundleJarFileName());
            return bundleJarFile;
        } else {
            throw new RuntimeException("Cannot find bundle " + bundleName);
        }
    }
}
