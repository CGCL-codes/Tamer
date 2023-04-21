package org.middleheaven.aas;

public class NameCallback implements Callback {

    private String prompt;

    private String defaultName;

    private String name;

    /**
	 * 
	 * @param prompt a label for the callback
	 * @param defaultName a name to present as default
	 */
    public NameCallback(String prompt, String defaultName) {
        this.prompt = prompt;
        this.defaultName = defaultName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getDefaultName() {
        return defaultName;
    }

    @Override
    public boolean isBlank() {
        return name == null || name.trim().isEmpty();
    }
}
