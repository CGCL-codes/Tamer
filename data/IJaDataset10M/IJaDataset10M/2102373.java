package jade.domain.FIPAAgentManagement;

import jade.content.onto.*;
import jade.content.schema.*;

/**
   This class groups into a separated ontology the elements of the 
   FIPA-Agent-Management-ontology (see FIPA specification document no. 23)
   representing generic exceptions. This allows reusing these 
   elements into other ontologies simply including tis Exception-ontology 
   <p>
   The actual <code>Ontology</code> object representing the 
   Exception-ontology is a singleton and is accessible through 
   the static method <code>getInstance()</code>
   
   @author Giovanni Caire - CSELT S.p.A.
   @version $Date: 2003-02-20 11:54:12 +0100 (gio, 20 feb 2003) $ $Revision: 3675 $
 */
public class ExceptionOntology extends Ontology implements ExceptionVocabulary {

    private static Ontology theInstance = new ExceptionOntology();

    /**
     This method returns the unique instance (according to the singleton 
     pattern) of the Exception-ontology.
     @return The singleton <code>Ontology</code> object, containing the 
     schemas for the elements of the Exception-ontology.
  */
    public static Ontology getInstance() {
        return theInstance;
    }

    private ExceptionOntology() {
        super(NAME, BasicOntology.getInstance(), new BCReflectiveIntrospector());
        try {
            add(new PredicateSchema(UNAUTHORISED), Unauthorised.class);
            add(new PredicateSchema(UNSUPPORTEDACT), UnsupportedAct.class);
            add(new PredicateSchema(UNEXPECTEDACT), UnexpectedAct.class);
            add(new PredicateSchema(UNSUPPORTEDVALUE), UnsupportedValue.class);
            add(new PredicateSchema(UNRECOGNISEDVALUE), UnrecognisedValue.class);
            add(new PredicateSchema(MISSINGARGUMENT), MissingArgument.class);
            add(new PredicateSchema(UNEXPECTEDARGUMENT), UnexpectedArgument.class);
            add(new PredicateSchema(UNEXPECTEDARGUMENTCOUNT), UnexpectedArgumentCount.class);
            add(new PredicateSchema(UNSUPPORTEDFUNCTION), UnsupportedFunction.class);
            add(new PredicateSchema(MISSINGPARAMETER), MissingParameter.class);
            add(new PredicateSchema(UNEXPECTEDPARAMETER), UnexpectedParameter.class);
            add(new PredicateSchema(UNRECOGNISEDPARAMETERVALUE), UnrecognisedParameterValue.class);
            add(new PredicateSchema(INTERNALERROR), InternalError.class);
            PredicateSchema ps = (PredicateSchema) getSchema(UNSUPPORTEDACT);
            ps.add(UNSUPPORTEDACT_ACT, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(UNEXPECTEDACT);
            ps.add(UNEXPECTEDACT_ACT, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(UNSUPPORTEDVALUE);
            ps.add(UNSUPPORTEDVALUE_VALUE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(UNRECOGNISEDVALUE);
            ps.add(UNRECOGNISEDVALUE_VALUE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(UNAUTHORISED);
            ps = (PredicateSchema) getSchema(UNSUPPORTEDFUNCTION);
            ps.add(UNSUPPORTEDFUNCTION_FUNCTION, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(MISSINGARGUMENT);
            ps.add(MISSINGARGUMENT_ARGUMENT, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(UNEXPECTEDARGUMENT);
            ps.add(UNEXPECTEDARGUMENT_ARGUMENT, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(MISSINGPARAMETER);
            ps.add(MISSINGPARAMETER_OBJECT_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps.add(MISSINGPARAMETER_PARAMETER_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(UNEXPECTEDPARAMETER);
            ps.add(UNEXPECTEDPARAMETER_OBJECT_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps.add(UNEXPECTEDPARAMETER_PARAMETER_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(UNRECOGNISEDPARAMETERVALUE);
            ps.add(UNRECOGNISEDPARAMETERVALUE_PARAMETER_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps.add(UNRECOGNISEDPARAMETERVALUE_PARAMETER_VALUE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            ps = (PredicateSchema) getSchema(INTERNALERROR);
            ps.add(INTERNALERROR_MESSAGE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
        } catch (OntologyException oe) {
            oe.printStackTrace();
        }
    }
}
