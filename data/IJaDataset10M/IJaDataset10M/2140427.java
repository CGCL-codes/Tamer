package grammarscope.exporter;

import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import grammarscope.parser.DefaultMutableGrammaticalRelations;
import grammarscope.parser.MutableGrammaticalRelation;
import grammarscope.parser.RelationModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Set existing grammatical relation model as expressed by target class to the data contained in this relation model. Match of relations operates on short
 * names. If there is no match nothing is done. This requires "tweaked" version of GrammaticalRelation class which adds some access functions.
 * 
 * @author Bernard Bou <bbou@ac-toulouse.fr>
 */
public class Setter {

    /**
	 * Set EnglishGrammaticalRelations model
	 * 
	 * @param thisRelationModel
	 *            source relation model
	 */
    public static void set(final RelationModel thisRelationModel) {
        Setter.set(thisRelationModel, EnglishGrammaticalRelations.class);
    }

    /**
	 * Set model
	 * 
	 * @param thisRelationModel
	 *            source relation model
	 * @param thisModelClassName
	 *            name target model class name (like "EnglishGrammaticalRelations")
	 */
    public static void set(final RelationModel thisRelationModel, final String thisModelClassName) {
        try {
            final Class<?> thisClass = Class.forName(thisModelClassName);
            Setter.set(thisRelationModel, thisClass);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Set model
	 * 
	 * @param thisRelationModel
	 *            source relation model
	 * @param thisModelClass
	 *            target model class (like EnglishGrammaticalRelations.class)
	 */
    public static void set(final RelationModel thisRelationModel, final Class<?> thisModelClass) {
        for (final MutableGrammaticalRelation thatRelation : thisRelationModel.theRelations) {
            final String thisName = thatRelation.getShortName();
            final boolean isAny = thatRelation.getId().equals("gov") || thatRelation.getId().equals("dep") || thatRelation.getId().equals("KILL");
            if (isAny) {
                continue;
            }
            GrammaticalRelation thisRelation = null;
            try {
                thisRelation = (GrammaticalRelation) Setter.invokeValueOf(thisModelClass, thisName);
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if (thisRelation != null) {
                thisRelation.setSourcePattern(thatRelation.getSourcePattern());
                thisRelation.setTargetPatterns(thatRelation.getTargetPatterns());
            }
        }
    }

    /**
	 * Dynamically invoke value (for lack of common interface)
	 * 
	 * @param thisClass
	 *            class
	 * @param thisName
	 *            name to find value of
	 * @return grammatical relations matching name
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    public static Object invokeValueOf(final Class<?> thisClass, final String thisName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        final Method thisMethod = thisClass.getMethod("valueOf", new Class<?>[] { String.class });
        return thisMethod.invoke(null, new Object[] { thisName });
    }

    /**
	 * Dump grammatical function
	 * 
	 * @param gr
	 *            grammatical function
	 */
    @SuppressWarnings("unchecked")
    private static void dump(final GrammaticalRelation gr) {
        System.out.println("GrammaticalRelation " + gr.getShortName());
        System.out.println("\tShort name:    " + gr.getShortName());
        System.out.println("\tLong name:     " + gr.getLongName());
        System.out.println("\tSpecific name: " + gr.getSpecific());
        System.out.println("\tSource pattern:	" + gr.getSourcePattern());
        for (final TregexPattern tp : (List<TregexPattern>) gr.getTargetPatterns()) {
            System.out.println("\tTarget pattern:	" + tp);
        }
    }

    public static void main(final String[] args) {
        final RelationModel thisModel = DefaultMutableGrammaticalRelations.makeDefaultModel();
        Setter.set(thisModel);
        for (final GrammaticalRelation thisRelation : EnglishGrammaticalRelations.values()) {
            Setter.dump(thisRelation);
        }
    }
}
