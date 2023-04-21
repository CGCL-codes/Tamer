package org.idspace.dk.aau.iwis.creativitytechnique;

import java.util.List;

public interface CreativityTechniqueFetcher {

    public void addCreativityTechnique(List<TechStmt> statement);

    public List<TechStmt> fetchStatements(String technique);

    public List<TechStmt> fetchAllStatements();

    public List<String> fetchTechniques();
}
