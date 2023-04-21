package edu.miami.cs.research.apg.generator.search.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import edu.miami.cs.research.apg.agregator.ontology.SongTrack;
import edu.miami.cs.research.apg.generator.search.representations.PlaylistNode;
import edu.miami.cs.research.apg.generator.search.representations.criteria.PlaylistCriteria;
import edu.miami.cs.research.apg.storage.SongTrackEntityManager;

/**
 * @author Darrius Serrant
 *
 */
public class Playlist_4th_GeneticAlgorithmVariant extends Playlist_3rd_GeneticAlgorithmVariant {

    /**
	 * @param criteria
	 * @param musicTitles
	 * @param db
	 */
    public Playlist_4th_GeneticAlgorithmVariant(PlaylistCriteria criteria, ArrayList<SongTrack> musicTitles, SongTrackEntityManager db) {
        super(criteria, musicTitles, db);
    }

    /**
	 * @param criteria
	 * @param musicTitles
	 * @param db
	 * @param targetCost
	 */
    public Playlist_4th_GeneticAlgorithmVariant(PlaylistCriteria criteria, ArrayList<SongTrack> musicTitles, SongTrackEntityManager db, double targetCost) {
        super(criteria, musicTitles, db, targetCost);
    }

    /**
	 * @param criteria
	 * @param musicTitles
	 * @param db
	 * @param targetCost
	 * @param mutationRate
	 * @param crossOverRate
	 * @param reproductionRate
	 */
    public Playlist_4th_GeneticAlgorithmVariant(PlaylistCriteria criteria, ArrayList<SongTrack> musicTitles, SongTrackEntityManager db, double targetCost, double mutationRate, double crossOverRate, double reproductionRate) {
        super(criteria, musicTitles, db, targetCost, mutationRate, crossOverRate, reproductionRate);
    }

    /**
	 * @param criteria
	 * @param musicTitles
	 * @param db
	 * @param targetCost
	 * @param mutationRate
	 * @param crossOverRate
	 * @param reproductionRate
	 * @param eliteReproductionRate
	 * @param eliteCrossOverRate
	 * @param eliteMutationRate
	 */
    public Playlist_4th_GeneticAlgorithmVariant(PlaylistCriteria criteria, ArrayList<SongTrack> musicTitles, SongTrackEntityManager db, double targetCost, double mutationRate, double crossOverRate, double reproductionRate, double eliteReproductionRate, double eliteCrossOverRate, double eliteMutationRate) {
        super(criteria, musicTitles, db, targetCost, mutationRate, crossOverRate, reproductionRate, eliteReproductionRate, eliteCrossOverRate, eliteMutationRate);
    }

    public boolean doSearch() {
        boolean solutionsFound = false;
        try {
            generateCandidates();
            numberOfIterations++;
            recordBenchmarks();
        } catch (Exception exception) {
        }
        while (!solutionsFound & candidates.size() >= 1) {
            PlaylistNode solution = (PlaylistNode) checkForGoal();
            if (solution != null) {
                solutionsFound = true;
                this.setBestPlaylist(solution);
                this.setSolution(solution.getLitePlaylist());
                break;
            } else {
                ArrayList<PlaylistNode> eligibleCandidates = selectFitCandidates();
                if (candidates.size() > 1) {
                    ArrayList<LivingPlaylistNode> toRemove = new ArrayList<LivingPlaylistNode>();
                    for (PlaylistNode livingP : candidates) {
                        ((LivingPlaylistNode) livingP).incrementAge();
                        if (!((LivingPlaylistNode) livingP).isAlive()) {
                            toRemove.add((LivingPlaylistNode) livingP);
                        }
                    }
                    candidates.removeAll(toRemove);
                    doPlaylistRecombination(eligibleCandidates);
                } else {
                    break;
                }
            }
            numberOfIterations++;
            recordBenchmarks();
        }
        recordBenchmarks();
        return solutionsFound;
    }

    protected void doPlaylistRecombination(ArrayList<PlaylistNode> eligibleCandidates) {
        Collections.sort(eligibleCandidates);
        ArrayList<PlaylistNode> elites = new ArrayList<PlaylistNode>();
        elites.add(eligibleCandidates.get(0));
        int i = 0;
        while (eligibleCandidates.get(0).getRating() == eligibleCandidates.get(i).getRating()) {
            elites.add(eligibleCandidates.get(i));
            i++;
            if (i == eligibleCandidates.size() - 1) {
                break;
            }
        }
        ArrayList<PlaylistNode> reproducers = new ArrayList<PlaylistNode>();
        ArrayList<PlaylistNode> mutators = new ArrayList<PlaylistNode>();
        ArrayList<PlaylistNode> crossers = new ArrayList<PlaylistNode>();
        Random generator = new Random(System.currentTimeMillis());
        double recombine;
        for (PlaylistNode elite : elites) {
            recombine = generator.nextDouble();
            if (recombine <= eliteMutationRate) {
                mutators.add(elite);
            } else if ((eliteMutationRate < recombine) && (recombine <= (eliteMutationRate + eliteCrossOverRate))) {
                crossers.add(elite);
            } else {
                reproducers.add(elite);
            }
        }
        eligibleCandidates.removeAll(elites);
        for (PlaylistNode candidate : eligibleCandidates) {
            recombine = generator.nextDouble();
            if (recombine <= mutationRate) {
                mutators.add(candidate);
            } else if ((mutationRate < recombine) && (recombine <= (mutationRate + crossOverRate))) {
                crossers.add(candidate);
            } else {
                reproducers.add(candidate);
            }
        }
        reproduce(reproducers);
        this.crossOver(crossers);
        this.mutateNow(mutators);
    }

    protected void crossOver(ArrayList<PlaylistNode> crossers) {
        HashSet<PlaylistNode[]> matingGroups = pairUpCandidates(crossers);
        ArrayList<PlaylistNode> liteCandidates = multiParentCrossOver(matingGroups);
        candidates.addAll(liteCandidates);
    }

    protected HashSet<PlaylistNode[]> pairUpCandidates(ArrayList<PlaylistNode> eligibleCandidates) {
        Collections.shuffle(eligibleCandidates);
        HashSet<PlaylistNode[]> pairs = new HashSet<PlaylistNode[]>();
        PlaylistNode[] pair = null;
        if (eligibleCandidates.size() % 2 != 0) {
            PlaylistNode first = candidates.remove(0);
            Random generator = new Random(System.currentTimeMillis());
            pair = new PlaylistNode[2];
            pair[0] = first;
            pair[1] = eligibleCandidates.get(generator.nextInt(eligibleCandidates.size() - 1));
            pairs.add(pair);
        }
        Iterator<PlaylistNode> cIterator = eligibleCandidates.iterator();
        while (cIterator.hasNext()) {
            pair = new PlaylistNode[2];
            pair[0] = cIterator.next();
            pair[1] = cIterator.next();
            pairs.add(pair);
        }
        return pairs;
    }

    protected ArrayList<PlaylistNode> multiParentCrossOver(HashSet<PlaylistNode[]> matingGroups) {
        Iterator<PlaylistNode[]> pairsIterator = matingGroups.iterator();
        ArrayList<PlaylistNode> liteCandidates = new ArrayList<PlaylistNode>();
        while (pairsIterator.hasNext()) {
            PlaylistNode[] pair = pairsIterator.next();
            HashSet<PlaylistNode> topPlaylists = offSpringLocalSearch(pair, 1);
            liteCandidates.addAll(topPlaylists);
        }
        return liteCandidates;
    }
}
