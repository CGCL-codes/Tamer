package com.granite.scc.common.lexicon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexicon implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id = null;

    private String name = null;

    private Map<String, Lexeme> nameToLexemeMap = null;

    public Lexicon() {
        this.initialize();
    }

    private void initialize() {
        this.nameToLexemeMap = new HashMap<String, Lexeme>();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addLexeme(Lexeme lexeme) {
        if (lexeme != null) {
            this.nameToLexemeMap.put(lexeme.getName(), lexeme);
        }
    }

    public Lexeme getLexeme(String name) {
        Lexeme lexeme = null;
        if (name != null) {
            lexeme = this.nameToLexemeMap.get(name);
        }
        return lexeme;
    }

    public List<Lexeme> getLexemeList() {
        return new ArrayList<Lexeme>(this.nameToLexemeMap.values());
    }
}
