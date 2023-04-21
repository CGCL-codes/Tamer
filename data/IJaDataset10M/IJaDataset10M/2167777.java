package toxTree.core;

import java.awt.Component;

public interface IDecisionCategoryEditor extends IToxTreeEditor {

    public void setCategory(IDecisionCategory category);

    public IDecisionCategory getCategory();

    public IDecisionCategory edit(Component owner, IDecisionCategory category);
}
