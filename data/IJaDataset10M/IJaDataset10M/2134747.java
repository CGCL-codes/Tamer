package model;

/**
 * Ein Agent ist ein zentrales Objekt und wird haupts�chlich durch seine
 * Accessibility-Relation beschrieben, die die Welt aus der Sicht des
 * Agenten angibt. <\br>
 * Jeder Agent kann einen Namen haben, wobei dieser nicht zwingend ist.
 * Die Agenten werden anhand ihrer <code>id</code> unterschieden. 
 * 
 * @author christian antic <e0525482[at]student.tuwien.ac.at>
 */
public class Agent {

    /**
	 * Jeder Agent kann einen Namen haben
	 */
    private String name;

    /**
	 * Zeigt an, ob der Agent aktiv ist, dh ob er im Graphen eingezeichnet und
	 * bei der Auswertung ber�cksichtigt wird.
	 */
    private boolean isActive = true;

    /**
	 * @param name
	 */
    public Agent(String name) {
        this.name = name;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return getName();
    }
}
