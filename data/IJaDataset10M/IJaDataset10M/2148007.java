package databaseVersionControl.domain.factory.composite;

import databaseVersionControl.domain.db.Sequence;
import databaseVersionControl.domain.db.SequenceComponent;
import databaseVersionControl.domain.ddl.CreateComposite;
import databaseVersionControl.infra.script.validation.CheckExistSequencePreInstallCondition;

public class CreateSequenceComposite extends CreateComposite<Sequence> implements SequenceComponent {

    private CheckExistSequencePreInstallCondition preInstallCondition;

    public CreateSequenceComposite(Sequence sequence) {
        super(sequence);
    }

    @Override
    public String getName() {
        return component.getName();
    }

    @Override
    public CreateSequenceComposite cache(Integer cache) {
        component.cache(cache);
        return this;
    }

    @Override
    public CreateSequenceComposite incrementBy(Integer incrementBy) {
        component.incrementBy(incrementBy);
        return this;
    }

    @Override
    public CreateSequenceComposite initialValue(Integer initialValue) {
        component.initialValue(initialValue);
        return this;
    }

    @Override
    public CreateSequenceComposite ifNotExists() {
        preInstallCondition = new CheckExistSequencePreInstallCondition(this);
        return this;
    }

    @Override
    public CreateSequenceComposite revokeIfNotExists() {
        preInstallCondition = null;
        return this;
    }

    @Override
    public CheckExistSequencePreInstallCondition getPreInstallCondition() {
        return preInstallCondition;
    }

    @Override
    public boolean hasPreInstallCondition() {
        return getPreInstallCondition() != null;
    }
}
