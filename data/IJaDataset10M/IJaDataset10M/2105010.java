package moten.david.xuml.model.example.tv;

import model.Class;
import model.Package;
import model.Primitive;
import moten.david.xuml.model.Multiplicity;
import moten.david.xuml.model.util.SystemBase;

public class Tv extends SystemBase {

    public Tv() {
        super(null, "Tv");
        initialise();
    }

    private void initialise() {
        Package pkg = createRootPackage("tv", "tv root package");
        Class channel = createClassWithArbitraryId(pkg, "Channel", "Digital TV channels");
        createAttribute(channel, "name").setUnique(true);
        createAttribute(channel, "description").setMandatory(false);
        Class recording = createClassWithArbitraryId(pkg, "Recording", "Scheduled and completed recordings");
        createAttribute(recording, "jobNumber", Primitive.INTEGER).setUnique(true);
        createAttribute(recording, "startTime", Primitive.TIMESTAMP);
        createAttribute(recording, "durationMinutes", Primitive.DECIMAL);
        createAttribute(recording, "name");
        createAttribute(recording, "description");
        createAssociation("R1", createAssociationEndPrimary(recording, Multiplicity.MANY, "is recorded by"), createAssociationEndSecondary(channel, Multiplicity.ONE, "records"));
    }
}
