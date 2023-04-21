package es.upm.dit;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import cross.reputation.model.Community;
import cross.reputation.model.CrossReputation;
import cross.reputation.model.Entity;
import cross.reputation.model.EntityIdentifier;
import cross.reputation.model.Evaluation;
import cross.reputation.model.GlobalModel;
import cross.reputation.model.Metric;
import cross.reputation.model.CommunityMetricToImport;

public class ReputationImporter {

    private static String urlServer = Property.getURL_SERVER();

    public static void main(String[] args) throws Exception {
        Ejecutor.ConfigureExtractorMode(Ejecutor.SCRAPPY_EXECUTOR_SERVER, urlServer);
        Ejecutor.ConfigureOpalServer("http://localhost/opal/opal.php");
        ConfigureModel.buildCrossReputationGlobalModel();
        Community destinationCommunity = GlobalModel.getCommunities().get("semanticWiki");
        for (Entity entity : ConfigureModel.SetWikiUserEntitiesAndAccounts()) {
            destinationCommunity.addEntityToAllMetrics(entity);
        }
        List<CommunityMetricToImport> metricsToImport = ConfigureModel.buildMetricsFromAllCommunitiesToAllMetrics(destinationCommunity);
        for (Entity entity : GlobalModel.getEntities().values()) {
            Map<Community, EntityIdentifier> communityEntity = entity.getIdentificatorInCommunities();
            for (Community community : communityEntity.keySet()) {
                String urlDomain = communityEntity.get(community).getUrl();
                if (urlDomain == null) {
                    System.out.println("Info:" + communityEntity.get(community).getName() + "(" + entity.getUniqueIdentificator() + ") has null url in:" + community.getName());
                    continue;
                }
                Map<Metric, Object> reputationMap = Scrapper.ExtractReputation(urlDomain);
                if (reputationMap == null || reputationMap.isEmpty()) {
                    continue;
                }
                if (reputationMap.size() == 1 && community.getMetrics().size() == 1) {
                    Metric metric = (Metric) community.getMetrics().toArray()[0];
                    for (Object value : reputationMap.values()) {
                        GlobalModel.addEvaluation(new Evaluation(community, entity, metric, value));
                        System.out.println("Ent:" + entity.getUniqueIdentificator() + " Com: " + community.getName() + " url:" + communityEntity.get(community).getUrl() + " met:" + metric.getIdentifier() + " rep:" + value);
                    }
                } else {
                    for (Metric metric : reputationMap.keySet()) {
                        Metric sourceMetric = null;
                        for (Metric comMetric : community.getMetrics()) {
                            if (metric == null || !metric.getIdentifier().equalsIgnoreCase(comMetric.getIdentifier())) {
                                continue;
                            }
                            sourceMetric = comMetric;
                            break;
                        }
                        if (sourceMetric == null) {
                            System.out.println("ERROR: metric parsed(" + (metric == null ? null : metric.getIdentifier()) + ") does not correspond to any metric of the community(" + community.getName() + "):" + community.getMetrics() + ". Its score is ignored");
                            continue;
                        }
                        GlobalModel.addEvaluation(new Evaluation(community, entity, sourceMetric, reputationMap.get(metric)));
                        System.out.println("Ent:" + entity.getUniqueIdentificator() + " Com:" + community.getName() + " url:" + communityEntity.get(community).getUrl() + " met:" + sourceMetric.getIdentifier() + " rep:" + reputationMap.get(metric));
                    }
                }
            }
        }
        System.out.println("\n*********************** Calculate Reputations***********************");
        CrossReputation crossReputation = new CrossReputation(destinationCommunity, metricsToImport, true, true);
        crossReputation.addAllEvaluations();
        crossReputation.calculateAllReputations();
        System.out.println("\n************************ Print Evaluations *************************");
        GlobalModel.printEvaluations();
    }
}
