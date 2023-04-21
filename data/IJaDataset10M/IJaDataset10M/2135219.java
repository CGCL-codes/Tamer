package astcentric.structure.bl;

public class CharacterData extends NumberData {

    private final char _character;

    public CharacterData(ConstructorDeclaration declaration, char character) {
        super(declaration);
        _character = character;
    }

    public final char getCharacter() {
        return _character;
    }

    @Override
    public double doubleValue() {
        return _character;
    }

    @Override
    public float floatValue() {
        return _character;
    }

    @Override
    Rank getRank() {
        return Rank.INTEGER;
    }

    @Override
    public int intValue() {
        return _character;
    }

    @Override
    public long longValue() {
        return _character;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CharacterData == false) {
            return false;
        }
        CharacterData characterData = (CharacterData) obj;
        return characterData._character == _character;
    }

    @Override
    public int hashCode() {
        return _character;
    }

    @Override
    public String toString() {
        return Character.toString(_character);
    }
}
