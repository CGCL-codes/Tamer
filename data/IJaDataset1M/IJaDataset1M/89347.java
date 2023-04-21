package br.unb.syntainia.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.unb.syntainia.entities.BestHit;
import br.unb.syntainia.entities.BestHitList;
import br.unb.syntainia.entities.GenesGroup;
import br.unb.syntainia.entities.Genome;
import br.unb.syntainia.entities.NoHitsList;

public class GenomeOrganizer {

    public Genome organize(GenomeConfiguration configuration, BestHitList bestHits, NoHitsList noHits) {
        Genome genome = null;
        HashMap<String, GenesGroup> genesGroupsMap = new HashMap<String, GenesGroup>();
        String groupName;
        GenesGroup group;
        Pattern subjectNamePattern = Pattern.compile(configuration.getFragmentPattern());
        Matcher matcher;
        for (BestHit bestHit : bestHits) {
            matcher = subjectNamePattern.matcher(bestHit.getSubjectName());
            if (matcher.matches()) {
                groupName = matcher.group(1);
                if (genesGroupsMap.containsKey(groupName)) {
                    group = genesGroupsMap.get(groupName);
                    group.addGene(bestHit);
                } else {
                    group = new GenesGroup(groupName);
                    group.addGene(bestHit);
                    genesGroupsMap.put(groupName, group);
                }
            }
        }
        genome = new Genome(configuration.getGenomeName());
        for (Iterator<GenesGroup> iterator = genesGroupsMap.values().iterator(); iterator.hasNext(); ) {
            genome.addGroup((GenesGroup) iterator.next());
        }
        genome.sortGroupsByName();
        genome.setNoHits(noHits);
        return genome;
    }
}
