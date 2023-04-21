package com.ohua.clustering;

import com.ohua.engine.multithreading.OneToOneIslandToSectionMapper;
import com.ohua.engine.multithreading.Section;

public class ClusteringOneToOneIslandToSectionMapper extends OneToOneIslandToSectionMapper {

    @Override
    protected Section createSection() {
        return new ClusteringSection();
    }
}
