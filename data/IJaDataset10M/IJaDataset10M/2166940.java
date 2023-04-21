package purej.dao.domain;

/**
 * 
 * DAO ������
 * 
 * @author leesangboo
 * 
 */
public class DAODomain {

    private String id;

    private String name;

    private String description;

    /**
     * @return description�� �����մϴ�.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *                �����Ϸ��� description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return id�� �����մϴ�.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *                �����Ϸ��� id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name�� �����մϴ�.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *                �����Ϸ��� name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
