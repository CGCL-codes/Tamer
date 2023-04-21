package org.proteored.miapeapi.factories.ge;

import org.proteored.miapeapi.cv.AdditionalInformationName;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ge.BufferComponentName;
import org.proteored.miapeapi.cv.ge.DirectDetectionAgentName;
import org.proteored.miapeapi.cv.ge.GelMatrixName;
import org.proteored.miapeapi.cv.ge.IndirectDetectionAgentName;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.XmlManager;

public class MiapeGEDocumentFactory {

    private MiapeGEDocumentFactory() {
    }

    ;

    public static MiapeGEDocumentBuilder createMiapeGEDocumentBuilder(Project project, String name, User user) {
        return new MiapeGEDocumentBuilder(project, name, user);
    }

    public static MiapeGEDocumentBuilder createMiapeGEDocumentBuilder(Project project, String name, User user, PersistenceManager db) {
        return new MiapeGEDocumentBuilder(project, name, user, db);
    }

    public static MiapeGEDocumentBuilder createMiapeGEDocumentBuilder(Project project, String name, User user, PersistenceManager db, XmlManager xmlManager, ControlVocabularyManager cvUtil) {
        return new MiapeGEDocumentBuilder(project, name, user, db, xmlManager, cvUtil);
    }

    public static DirectDetectionBuilder createDirectDetectionBuilder(String name) {
        return new DirectDetectionBuilder(name);
    }

    /**
	 * The name should be one of the possible values in
	 * {@link DirectDetectionAgentName}
	 * 
	 * @param name
	 */
    public static DirectDetectionAgentBuilder createDirectDetectionAgentBuilder(String name) {
        return new DirectDetectionAgentBuilder(name);
    }

    /**
	 * Set the name of the agent used in the indirect detection. It should be
	 * one of the possible values from {@link IndirectDetectionAgentName}
	 */
    public static IndirectDetectionAgentBuilder createIndirectDetectionAgentBuilder(String name) {
        return new IndirectDetectionAgentBuilder(name);
    }

    public static IndirectDetectionBuilder createIndirectDetectionBuilder(String name) {
        return new IndirectDetectionBuilder(name);
    }

    /**
	 * Set the additional information. It should be one of the possible values
	 * from {@link AdditionalInformationName}
	 * 
	 */
    public static AdditionalInformationBuilder createAdditionalInformationBuilder(String name) {
        return new AdditionalInformationBuilder(name);
    }

    public static ImageGelElectrophoresisBuilder createImageBuilder(String name) {
        return new ImageGelElectrophoresisBuilder(name);
    }

    public static ImageAcquisitionBuilder createImageAcquisitionBuilder(String name) {
        return new ImageAcquisitionBuilder(name);
    }

    public static ImageAcquisitionEquipmentBuilder createImageAcquisitionEquipmentBuilder(String name) {
        return new ImageAcquisitionEquipmentBuilder(name);
    }

    public static ProtocolBuilder createProtocolBuilder(String name) {
        return new ProtocolBuilder(name);
    }

    public static DimensionBuilder createDimensionBuilder(String name) {
        return new DimensionBuilder(name);
    }

    public static InterdimensionProcessBuilder createInterdimensionProcessBuilder(String name) {
        return new InterdimensionProcessBuilder(name);
    }

    public static ElectrophoresisProtocolBuilder createElectrophoresisProtocolBuilder(String name) {
        return new ElectrophoresisProtocolBuilder(name);
    }

    public static SampleBuilder createSampleBuilder(String name) {
        return new SampleBuilder(name);
    }

    public static LaneBuilder createLaneBuilder(String laneName) {
        return new LaneBuilder(laneName);
    }

    public static ImageGelElectrophoresisBuilder createImageGelElectrophoresisBuilder(String imageName) {
        return new ImageGelElectrophoresisBuilder(imageName);
    }

    public static BufferBuilder createBufferBuilder(String name) {
        return new BufferBuilder(name);
    }

    /**
	 * The name should be one of the possible values in
	 * {@link BufferComponentName}
	 * 
	 * @param componentName
	 */
    public static BufferComponentBuilder createBufferComponentBuilder(String componentName) {
        return new BufferComponentBuilder(componentName);
    }

    /**
	 * Sets the gel matrix name. It should be one of the possible values of
	 * {@link GelMatrixName}
	 */
    public static GelMatrixBuilder createGelMatrixBuilder(String gelMatrixName) {
        return new GelMatrixBuilder(gelMatrixName);
    }

    public static SampleApplicationBuilder createSampleApplicationBuilder(String sampleApplicationName) {
        return new SampleApplicationBuilder(sampleApplicationName);
    }
}
