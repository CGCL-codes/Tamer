package net.sourceforge.ondex.transformer.weights;

import java.util.HashSet;
import java.util.Set;
import net.sourceforge.ondex.args.ArgumentDefinition;
import net.sourceforge.ondex.args.StringArgumentDefinition;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.GDS;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.transformer.AbstractONDEXTransformer;
import net.sourceforge.ondex.xten.functions.MdHelper;

/**
 * 
 * @author lysenkoa
 *
 */
public class Transformer extends AbstractONDEXTransformer {

    @Override
    public ArgumentDefinition<?>[] getArgumentDefinitions() {
        return new ArgumentDefinition<?>[] { new StringArgumentDefinition("CorrelationAtt", "Correlation attribute name", false, "Correlation", false), new StringArgumentDefinition("hssAtt", "% overal attribute name", false, "COVERAGE", false) };
    }

    @Override
    public String getName() {
        return "DILS 2010 paper, wieghts transformer";
    }

    @Override
    public String getVersion() {
        return "v1.0";
    }

    @Override
    public boolean requiresIndexedGraph() {
        return false;
    }

    @Override
    public String[] requiresValidators() {
        return new String[0];
    }

    @Override
    public void start() throws Exception {
        Object temp = ta.getUniqueValue("CorrelationAtt");
        if (temp != null) {
            String corrRTId = temp.toString();
            AttributeName corr = MdHelper.createAttName(graph, corrRTId, Double.class);
            AttributeName absCorr = MdHelper.createAttName(graph, "Abs_corr", Double.class);
            ONDEXView<ONDEXRelation> rs = graph.getRelationsOfAttributeName(corr);
            while (rs.hasNext()) {
                ONDEXRelation r = rs.next();
                GDS<ONDEXRelation> gds = r.getRelationGDS(corr);
                if (gds != null) {
                    Double value = (Double) gds.getValue();
                    if (value < 0d) {
                        value = -value;
                    }
                    r.createRelationGDS(absCorr, value, false);
                }
            }
            rs.close();
        }
        temp = ta.getUniqueValue("hssAtt");
        if (temp != null) {
            String hssRTId = temp.toString();
            AttributeName hss = MdHelper.createAttName(graph, hssRTId, Double.class);
            AttributeName hssWeight = MdHelper.createAttName(graph, "PSI", Double.class);
            Set<Integer> toDelete = new HashSet<Integer>();
            ONDEXView<ONDEXRelation> rs = graph.getRelationsOfAttributeName(hss);
            Set<Integer> toKeep = new HashSet<Integer>();
            while (rs.hasNext()) {
                ONDEXRelation r = rs.next();
                if (toKeep.contains(r.getId()) || toDelete.contains(r.getId())) {
                    continue;
                }
                GDS<ONDEXRelation> gds = r.getRelationGDS(hss);
                Double value = (Double) gds.getValue();
                Double value2 = null;
                ONDEXRelation z = graph.getRelation(r.getToConcept(), r.getFromConcept(), r.getOfType());
                if (z != null) {
                    toDelete.add(z.getId());
                    GDS<ONDEXRelation> gds2 = z.getRelationGDS(hss);
                    if (gds2 != null) {
                        value2 = (Double) gds2.getValue();
                    }
                }
                if (value2 != null) {
                    value = Math.max(value, value2);
                }
                r.createRelationGDS(hssWeight, value, false);
                toKeep.add(r.getId());
            }
            rs.close();
            for (Integer id : toDelete) {
                graph.deleteRelation(id);
            }
        }
    }
}
