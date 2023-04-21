package de.ifgi.simcat.reasoner.msc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import de.ifgi.simcat.reasoner.ABox;
import de.ifgi.simcat.reasoner.Assertion;
import de.ifgi.simcat.reasoner.Individual;
import de.ifgi.simcat.reasoner.Role;
import de.ifgi.simcat.reasoner.RoleAssertion;

public class IndividualDescriptionTree {

    private Collection<IndividualVertex> vertices;

    private Collection<IndividualEdge> edges;

    private IndividualVertex rootVertex;

    private ArrayList<String> wallStructures;

    public IndividualDescriptionTree(Individual indy, ABox aBox) {
        wallStructures = new ArrayList<String>();
        wallStructures.add("arete");
        wallStructures.add("chimney");
        wallStructures.add("couloir");
        wallStructures.add("crack");
        wallStructures.add("dihedral");
        wallStructures.add("ledge");
        wallStructures.add("ramp");
        wallStructures.add("niche");
        wallStructures.add("overhang");
        wallStructures.add("roof");
        wallStructures.add("slab");
        wallStructures.add("wall");
        createDescriptionTree(indy, aBox);
    }

    private void createDescriptionTree(Individual indy, ABox aBox) {
        vertices = new ArrayList<IndividualVertex>();
        edges = new ArrayList<IndividualEdge>();
        ArrayList<Assertion> roleAssertions = aBox.getAssertionsByRelatingIndividual(indy).getAssertionsByType(RoleAssertion.class).getAssertionList();
        rootVertex = addVertex(indy, false);
        buildSubTrees(rootVertex, roleAssertions, aBox);
    }

    private void addNonOccuringWallStructures(IndividualVertex fromVertex, ABox aBox) {
        for (String wallStructure : wallStructures) {
            IndividualVertex toVertex = addVertex(aBox.getIndividualByName(wallStructure), true);
            addEdge(IndividualEdge.UNIVERSAL, new Role("containsWallStructure"), fromVertex, toVertex);
        }
    }

    private void buildSubTrees(IndividualVertex fromVertex, ArrayList<Assertion> assertions, ABox aBox) {
        if (!(assertions.size() == 0)) {
            for (Assertion assertion : assertions) {
                if (assertion instanceof RoleAssertion) {
                    Role role = ((RoleAssertion) assertion).getRole();
                    Individual relatedIndy = ((RoleAssertion) assertion).getRelatedIndividual();
                    if ("containsWallStructure".equals(role.getName())) {
                        int removeIndex = -1;
                        for (int i = 0; i < wallStructures.size(); i++) {
                            if (wallStructures.get(i).equals(relatedIndy.getName())) {
                                removeIndex = i;
                                break;
                            }
                        }
                        if (removeIndex != -1) wallStructures.remove(removeIndex);
                    }
                    ArrayList<Assertion> roleAssertions = aBox.getAssertionsByRelatingIndividual(relatedIndy).getAssertionsByType(RoleAssertion.class).getAssertionList();
                    IndividualVertex toVertex = addVertex(relatedIndy, false);
                    addEdge(IndividualEdge.EXISTENTIAL, role, fromVertex, toVertex);
                    buildSubTrees(toVertex, roleAssertions, aBox);
                }
            }
        }
    }

    public IndividualVertex addVertex(Individual indy, boolean isNegated) {
        IndividualVertex vertex = new IndividualVertex(indy, isNegated);
        vertices.add(vertex);
        return vertex;
    }

    public void addEdge(String restrictionType, Role role, IndividualVertex fromVertex, IndividualVertex toVertex) {
        edges.add(new IndividualEdge(restrictionType, role, fromVertex, toVertex));
    }

    public IndividualVertex getRootVertex() {
        return rootVertex;
    }
}
