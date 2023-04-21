package cross.reputation.model;

import java.util.ArrayList;
import java.util.List;

public class ReputationTransformer {

    private Community destineCommunity;

    private List<Entity> entities = new ArrayList<Entity>();

    private List<CommunityMetricToImport> metricsFromCommunities = new ArrayList<CommunityMetricToImport>();

    ReputationTransformer(Community destineComunity, List<Entity> entities, List<CommunityMetricToImport> metricsFromCommunities) {
        this.destineCommunity = destineComunity;
        this.entities = entities;
        this.metricsFromCommunities = metricsFromCommunities;
    }

    public Community getDestineCommunity() {
        return destineCommunity;
    }

    public void setDestineCommunity(Community destineCommunity) {
        this.destineCommunity = destineCommunity;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<CommunityMetricToImport> getMetricsFromCommunities() {
        return metricsFromCommunities;
    }

    public void setMetricsFromCommunities(List<CommunityMetricToImport> metricsFromCommunities) {
        this.metricsFromCommunities = metricsFromCommunities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void addAllDestinationEntities() {
        for (Entity entity : destineCommunity.getEntities().keySet()) {
            entities.add(entity);
        }
    }

    public void addAllEntities() {
        for (CommunityMetricToImport community : metricsFromCommunities) {
            for (Entity entity : community.getCommunity().getEntities().keySet()) {
                entities.add(entity);
            }
        }
    }

    public void addMetricFromCommunity(CommunityMetricToImport communityMetricToImport) {
        metricsFromCommunities.add(communityMetricToImport);
    }
}
