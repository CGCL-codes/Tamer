package br.unb.unbiquitous.ubiquitos.uosontoapp;

import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.Gateway;
import br.unb.unbiquitous.ubiquitos.uos.application.UosApplication;
import br.unb.unbiquitous.ubiquitos.uos.ontologyEngine.api.OntologyDeploy;
import br.unb.unbiquitous.ubiquitos.uos.ontologyEngine.api.OntologyStart;
import br.unb.unbiquitous.ubiquitos.uos.ontologyEngine.api.OntologyUndeploy;

/**
 *
 * @author anaozaki
 */
public class TesteApp implements UosApplication {

    public void start(Gateway gateway, OntologyStart ontology) {
        ontology.getOntologyDeployClass().addClass("entity");
        ontology.getOntologyDeployClass().addSubClass("physical", "entity");
        ontology.getOntologyDeployClass().addSubClass("logical", "entity");
        ontology.getOntologyDeployClass().addSubClass("application", "logical");
        ontology.getOntologyDeployClass().addSubClass("driver", "logical");
        ontology.getOntologyDeployClass().addSubClass("user", "physical");
        ontology.getOntologyDeployClass().addSubClass("resource", "physical");
        ontology.getOntologyDeployClass().addSubClass("input", "resource");
        ontology.getOntologyDeployClass().addSubClass("output", "resource");
        ontology.getOntologyDeployClass().addSubClass("environment", "resource");
        ontology.getOntologyDeployClass().addSubClass("output_sound", "output");
        ontology.getOntologyDeployClass().addSubClass("output_video", "output");
        ontology.getOntologyDeployClass().addSubClass("output_text", "output");
        ontology.getOntologyDeployClass().addSubClass("light", "environment");
        ontology.getOntologyDeployClass().addSubClass("temperature", "environment");
        ontology.getOntologyDeployClass().addSubClass("input_sound", "input");
        ontology.getOntologyDeployClass().addSubClass("input_video", "input");
        ontology.getOntologyDeployClass().addSubClass("input_text", "input");
        ontology.getOntologyDeployClass().addSubClass("pointer", "input");
        ontology.getOntologyInstance().addInstanceOf("printerName", "output_text");
        ontology.getOntologyInstance().addInstanceOf("monitorName", "output_video");
        ontology.getOntologyInstance().addInstanceOf("cameraName", "input_video");
        ontology.getOntologyInstance().addInstanceOf("airConditionairName", "temperature");
        ontology.getOntologyInstance().addInstanceOf("lightName", "light");
        for (int i = 0; i < 10; i++) {
            ontology.getOntologyInstance().addInstanceOf("monitorName" + i, "output_video");
            ontology.getOntologyInstance().addInstanceOf("keyboardName" + i, "input_text");
            ontology.getOntologyInstance().addInstanceOf("pointerName" + i, "pointer");
        }
        for (int i = 0; i < 15; i++) {
            ontology.getOntologyInstance().addInstanceOf("userName" + i, "user");
        }
        for (int i = 0; i < 8; i++) {
            ontology.getOntologyInstance().addInstanceOf("driverName" + i, "driver");
        }
        ontology.getOntologyDeployClass().addClass("disciplina");
        ontology.getOntologyDeployClass().addClass("turma");
        ontology.getOntologyInstance().addInstanceOf("BD", "disciplina");
        ontology.getOntologyInstance().addInstanceOf("A", "turma");
        ((OntologyDeploy) ontology).getOntologyDeployObjectProperty().addObjectProperty("temTurma");
        ontology.getOntologyInstance().addObjectPropertyAssertion("BD", "temTurma", "A");
        ontology.saveChanges();
        ((OntologyUndeploy) ontology).getOntologyUndeployClass().removeClass("turma");
        ontology.saveChanges();
    }

    public void stop() throws Exception {
    }

    public void init(OntologyDeploy ontology) {
    }

    public void tearDown(OntologyUndeploy ontology) throws Exception {
    }
}
