package iConfWeb.bean.confsettings;

import business.domain.Criterion;

public class CriterionBean extends Criterion {

    private int weight;

    public CriterionBean() {
    }

    public CriterionBean(Criterion c) {
        this.setName(c.getName());
        this.setDescription(c.getDescription());
    }

    /**
	 * @return the weight
	 */
    public int getWeight() {
        return weight;
    }

    /**
	 * @param weight the weight to set
	 */
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
