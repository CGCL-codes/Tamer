package eu.planets_project.pp.plato.services.action.minimee;

import at.tuwien.minimee.migration.MigrationService;
import eu.planets_project.pp.plato.model.DigitalObject;
import eu.planets_project.pp.plato.model.PreservationActionDefinition;
import eu.planets_project.pp.plato.model.SampleObject;
import eu.planets_project.pp.plato.model.measurement.Measurement;
import eu.planets_project.pp.plato.services.PlatoServiceException;
import eu.planets_project.pp.plato.services.action.IMigrationAction;
import eu.planets_project.pp.plato.services.action.MigrationResult;

public class MiniMeeMigrationService implements IMigrationAction {

    /**
     * @return null
     */
    public MigrationResult getLastResult() {
        return null;
    }

    public MigrationResult migrate(PreservationActionDefinition action, SampleObject sampleObject) throws PlatoServiceException {
        MigrationService service = new MigrationService();
        long start = System.nanoTime();
        String settings = "";
        if (action.isExecute()) {
            settings = action.getParamByName("settings");
        }
        MigrationResult result = service.migrate(sampleObject.getData().getData(), action.getUrl(), settings);
        setResultName(result, sampleObject);
        long duration = (System.nanoTime() - start) / (1000000);
        service.addExperience(result.getFeedbackKey(), action.getUrl(), new Measurement("roundtripTimeMS", new Double(duration)));
        return result;
    }

    /**
     * The name of the resultObject is not very nice, and sometime not set at all.
     * Therefore we create a new name, based on the name of the sampleObject 
     * and the name of the target format 
     * 
     * @param result
     * @param sampleObject
     */
    private void setResultName(MigrationResult result, SampleObject sampleObject) {
        DigitalObject resultObject = result.getMigratedObject();
        if (resultObject != null) {
            String resultName = "result.";
            if (sampleObject.getFullname() != null) {
                resultName = sampleObject.getFullname() + ".";
            }
            if (result.getTargetFormat() != null) {
                resultName = resultName + result.getTargetFormat().getDefaultExtension();
                resultObject.getFormatInfo().assignValues(result.getTargetFormat());
            }
            resultObject.setFullname(resultName);
        }
    }

    public boolean perform(PreservationActionDefinition action, SampleObject sampleObject) throws PlatoServiceException {
        migrate(action, sampleObject);
        return true;
    }
}
