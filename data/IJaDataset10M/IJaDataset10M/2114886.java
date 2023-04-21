package com.ecortex.emergent.api;

class EmergentType {

    private static final String Quote = "\"";

    public static Object ConvertString(String Value, Class<?> ConvertClass) {
        Object Result = null;
        try {
            if (ConvertClass.equals(String.class)) Result = RemoveQuotes(Value); else if (ConvertClass.equals(Integer.class)) Result = Integer.parseInt(Value); else if (ConvertClass.equals(Float.class)) Result = Float.parseFloat(Value); else if (ConvertClass.equals(Double.class)) Result = Double.parseDouble(Value); else if (ConvertClass.equals(Boolean.class)) Result = Boolean.parseBoolean(Value); else Result = Value;
        } catch (NumberFormatException e) {
            throw new EmergentException("Returned data (" + Value + ") is not of type " + ConvertClass.toString(), e);
        }
        return Result;
    }

    public static String FormatObject(Object Value) {
        String Result = " ";
        if (Value != null) {
            Result = Value.toString();
            if (Value.getClass().equals(String.class)) Result = AddQuotes(Result);
        }
        return Result;
    }

    public static Integer ArrayDimensions(Object a) {
        Integer Dimensions = 0;
        Class c = a.getClass();
        while (c.isArray()) {
            Dimensions++;
            c = c.getComponentType();
        }
        return Dimensions;
    }

    public static Class GetBaseClass(Object a) {
        Class c = a.getClass();
        while (c.isArray()) c = c.getComponentType();
        return c;
    }

    public static String Delimit(String[] Elements, String Delimiter) {
        String Result = "";
        for (int i = 0; i < Elements.length; i++) {
            if (i > 0) Result += Delimiter;
            Result += Elements[i];
        }
        return Result;
    }

    public static String RemoveQuotes(String Raw) {
        String Out = Raw;
        if (Out.startsWith(Quote)) Out = Out.substring(1);
        if (Out.endsWith(Quote)) Out = Out.substring(0, Out.length() - 1);
        return Out;
    }

    public static String AddQuotes(String Raw) {
        return Quote + Raw + Quote;
    }
}
