package collaboration.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Entity
* @author ontology bean generator
* @version 2009/06/29, 22:52:40
*/
public class Entity implements Concept {

    /**
* Protege name: idEntity
   */
    private Id idEntity;

    public void setIdEntity(Id value) {
        this.idEntity = value;
    }

    public Id getIdEntity() {
        return this.idEntity;
    }

    /**
* Protege name: descriptionEntity
   */
    private Description descriptionEntity;

    public void setDescriptionEntity(Description value) {
        this.descriptionEntity = value;
    }

    public Description getDescriptionEntity() {
        return this.descriptionEntity;
    }
}
