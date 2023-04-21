package gov.ornl.nice.niceitem.item.jobLauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import gov.ornl.nice.nicedatastructures.form.AllowedValueType;
import gov.ornl.nice.nicedatastructures.form.DataComponent;
import gov.ornl.nice.nicedatastructures.form.Entry;
import gov.ornl.nice.nicedatastructures.form.Form;
import gov.ornl.nice.nicedatastructures.form.OutputComponent;
import gov.ornl.nice.nicedatastructures.resource.NiCEResource;

/** 
 * <!-- begin-UML-doc -->
 * <p>The JobLauncherForm is a subclass of Form that is specialized to work with the JobLauncher Item. It contains a DataComponent with two Entries, one for an input file and one for the hostname of the machine on which the job should be launched. The DataComponents are named "Input File" and "Platform" respectively. It also contains one OutputComponent.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class JobLauncherForm extends Form {

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The constructor.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public JobLauncherForm() {
        super();
        File dir = new File(".");
        final String[] files = dir.list();
        Entry fileEntry = new Entry() {

            public void setup() {
                if (files != null) {
                    for (String i : files) allowedValues.add(i);
                    defaultValue = allowedValues.get(0);
                    allowedValueType = AllowedValueType.Discrete;
                }
                return;
            }
        };
        fileEntry.setDescription("The input file that should be used in the launch.");
        fileEntry.setName("Input File");
        fileEntry.setId(1);
        Entry platformEntry = new Entry() {

            public void setup() {
                this.allowedValues.add("antecessor.ornl.gov");
                this.allowedValues.add("habilis.ornl.gov");
                this.allowedValueType = AllowedValueType.Discrete;
                this.defaultValue = this.allowedValues.get(0);
                return;
            }
        };
        platformEntry.setDescription("The platform on which the job " + "should be launched.");
        platformEntry.setName("Platform");
        platformEntry.setId(2);
        setName("Job Launcher");
        setDescription("Generic Job Launch Form");
        DataComponent fileComponent = new DataComponent();
        fileComponent.setId(1);
        fileComponent.setDescription("This section contains the name of the file " + "and the identity of the computing platform that should " + "be used by the Item.");
        fileComponent.setName("Input File Name and Computing Platform");
        fileComponent.addEntry(fileEntry);
        fileComponent.addEntry(platformEntry);
        addComponent(fileComponent);
        OutputComponent outputData = new OutputComponent();
        outputData.setName("Output Files and Data");
        outputData.setId(2);
        outputData.setDescription("This section describes all of the data " + "and additional output created by the job launch.");
        addComponent(outputData);
        return;
    }
}
