package de.sonivis.tool.ontology.view.graphcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import ca.odell.glazedlists.EventList;
import de.sonivis.tool.core.metricsystem.AbstractMetric;
import de.sonivis.tool.core.metricsystem.EdgeMetric;
import de.sonivis.tool.core.metricsystem.MetricCategory;
import de.sonivis.tool.core.metricsystem.MetricManager;

/**
 * Provides proposed metrics for GUI text or combo controls.
 * 
 * @author Sebastian
 * @author mario
 */
public class NumericEdgeMetricProposalProvider extends SimpleContentProposalProvider {

    public NumericEdgeMetricProposalProvider() {
        super(new String[] {});
        setFiltering(true);
    }

    @Override
    public final IContentProposal[] getProposals(final String contents, final int position) {
        EventList<AbstractMetric<?, ?>> metrics = MetricManager.getInstance().getMetrics();
        metrics.getReadWriteLock().readLock().lock();
        try {
            final Collection<AbstractMetric<?, ?>> activatedMetrics = getPossibleMetrics();
            final ArrayList<IContentProposal> resultingProposals = new ArrayList<IContentProposal>();
            for (final AbstractMetric<?, ?> metric : activatedMetrics) {
                final String proposal = metric.getName();
                final String proposalDescription = metric.getDescription();
                if (proposal.length() >= contents.length() && proposal.toLowerCase().contains(contents.toLowerCase())) {
                    resultingProposals.add(new IContentProposal() {

                        public String getContent() {
                            return proposal;
                        }

                        public String getDescription() {
                            return ("".equals(proposalDescription)) ? null : proposalDescription;
                        }

                        public String getLabel() {
                            return null;
                        }

                        public int getCursorPosition() {
                            return proposal.length();
                        }
                    });
                }
            }
            return resultingProposals.toArray(new IContentProposal[resultingProposals.size()]);
        } finally {
            metrics.getReadWriteLock().readLock().unlock();
        }
    }

    private Collection<AbstractMetric<?, ?>> getPossibleMetrics() {
        final Collection<AbstractMetric<?, ?>> proposedMetrics = new HashSet<AbstractMetric<?, ?>>();
        final Collection<EdgeMetric<?>> metrics = MetricCategory.EDGE_METRICS.getMetrics();
        synchronized (metrics) {
            for (final AbstractMetric<?, ?> metric : metrics) {
                if ((metric.getValueType().equals(double.class) || metric.getValueType().equals(int.class) || metric.getValueType().equals(Integer.class) || metric.getValueType().equals(float.class) || metric.getValueType().equals(Float.class)) && metric.isActivated()) {
                    proposedMetrics.add(metric);
                }
            }
        }
        return proposedMetrics;
    }
}
