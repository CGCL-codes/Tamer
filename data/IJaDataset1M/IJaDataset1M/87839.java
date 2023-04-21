package pcgen.cdom.base;

public interface BasicChoice<T> {

    public void setChoiceActor(ChoiceActor<T> actor);

    public ChoiceActor<T> getChoiceActor();
}
