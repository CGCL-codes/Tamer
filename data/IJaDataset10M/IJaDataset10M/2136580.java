package com.tt.bnct.dao;

import com.tt.bnct.domain.Enzyme;
import com.tt.bnct.domain.PathwayEntry;

public interface EnzymeDao {

    void createEnzyme(Enzyme enzyme);

    Enzyme findEnzyme(String enzymeId, PathwayEntry pathwayEntry);

    void deleteEnzyme(String enzymeId);
}
