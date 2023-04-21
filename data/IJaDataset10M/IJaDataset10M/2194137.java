package dbs_project.myDB.abstraction;

public class IdGene implements IdGenerator {

    private int lastId;

    public IdGene(int lastId) {
        this.lastId = lastId;
    }

    /**
	 * @return the new id
	 */
    public int newId() {
        lastId++;
        return lastId;
    }

    /**
	 * @return the last id
	 */
    public int getLastId() {
        return lastId;
    }
}
