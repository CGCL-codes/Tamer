package org.codegallery.javagal.concurrent.jt.ch03;

public interface CharacterSource {

    public void addCharacterListener(CharacterListener cl);

    public void removeCharacterListener(CharacterListener cl);

    public void nextCharacter();
}
