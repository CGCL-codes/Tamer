package mafiagame;

public class RoleDefinition {

    public enum DefinitionType {

        SAVING, KILLING, ODDBALL, GAMECHANGING, INVESTIGATIVE, MANIPULATIVE, LINKED
    }

    ;

    public DefinitionType getDefinitionType() {
        return null;
    }

    public String explainRole() {
        return null;
    }

    public boolean canBeAssigned(Player p) {
        return true;
    }

    public void assignRole(Game g, Player p) {
        return;
    }

    public String getName() {
        return null;
    }
}
