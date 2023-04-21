package br.upe.dsc.caeto.core.cacheunit;

public class CacheUnit {

    public static final String DATA_TYPE = "data";

    public static final String INSTRUCTION_TYPE = "instruction";

    public static final String DATA_CACHE_LEVEL_1 = "dl1";

    public static final String DATA_CACHE_LEVEL_2 = "dl2";

    public static final String INSTRUCTION_CACHE_LEVEL_1 = "il1";

    public static final String INSTRUCTION_CACHE_LEVEL_2 = "il2";

    public static final String UNIFIED_CACHE_LEVEL_1 = "L1";

    public static final String UNIFIED_CACHE_LEVEL_2 = "L2";

    private String name;

    public CacheUnit(String nome) {
        this.name = nome;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object object) {
        return (object instanceof CacheUnit) ? this.name.equals(((CacheUnit) object).getName()) : false;
    }
}
