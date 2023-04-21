package com.dexter.fridgeManagement.client;

import java.util.ArrayList;
import java.util.Collection;
import com.dexter.fridgeManagement.shared.ShoppingListEntry;
import com.google.gwt.core.client.JsArray;

final class JSONConverter {

    /**
     * Converts an input JSON string to a list of objects
     * @param text_in the JSON string 
     * @return the resulting list. Will be empty if the string was null or empty.
     */
    public Collection<Unit> getUnits(String text_in) {
        Collection<Unit> listOfUnits = new ArrayList<Unit>();
        JsArray<UnitJsData> jsObjects = convertJsonToUnitJsData(text_in);
        for (int i = 0; i < jsObjects.length(); i++) {
            Unit convertedItem = JSONConverter.convertToUnit(jsObjects.get(i));
            listOfUnits.add(convertedItem);
        }
        return listOfUnits;
    }

    /**
     * Converts the JS object to a POJO object 
     * @param unitJsData_in the JS object to be converted
     * @return the converted object
     */
    private static Unit convertToUnit(UnitJsData unitJsData_in) {
        Unit unit = new Unit(URLHandler.decodeString(unitJsData_in.getName()));
        return unit;
    }

    /**
     * Converts the JS object to a POJO object 
     * @param productJsData_in the JS object to be converted
     * @return the converted object
     */
    private static Product convertToProduct(ProductJsData productJsData_in) {
        Product product = new Product(URLHandler.decodeString(productJsData_in.getName()), URLHandler.decodeString(productJsData_in.getCategory()), URLHandler.decodeString(productJsData_in.getUnit()), productJsData_in.getMinimalAmount());
        return product;
    }

    /**
     * Converts a JSON string to an array of JS objects
     * @param jsonString_in the string to be converted
     * @return the converted array
     */
    private final native JsArray<UnitJsData> convertJsonToUnitJsData(String jsonString_in);

    private final native JsArray<ProductJsData> convertJsonToProductJsData(String jsonString_in);

    public Collection<Product> getProducts(String text_in) {
        Collection<Product> listOfProducts = new ArrayList<Product>();
        JsArray<ProductJsData> jsObjects = convertJsonToProductJsData(text_in);
        for (int i = 0; i < jsObjects.length(); i++) {
            Product convertedItem = JSONConverter.convertToProduct(jsObjects.get(i));
            listOfProducts.add(convertedItem);
        }
        return listOfProducts;
    }

    /**
     * Converts the given JSON string to a collection of objects
     * @param text_in - a JSON string that encodes objects of the same type as in the result collection
     * @return the values from the JSON string, converted in objects
     */
    public Collection<ShoppingListEntry> getShoppingListEntries(String text_in) {
        Collection<ShoppingListEntry> listOfShoppingListEntries = new ArrayList<ShoppingListEntry>();
        JsArray<ShoppingListJsObject> jsObjects = convertJsonToShoppingListJsData(text_in);
        for (int i = 0; i < jsObjects.length(); i++) {
            ShoppingListEntry convertedItem = JSONConverter.convertToShoppingListEntry(jsObjects.get(i));
            listOfShoppingListEntries.add(convertedItem);
        }
        return listOfShoppingListEntries;
    }

    /**
     * Converts the JS object to a POJO object 
     * @param shoppingListJsData_in the JS object to be converted
     * @return the converted object
     */
    private static ShoppingListEntry convertToShoppingListEntry(ShoppingListJsObject shoppingListJsData_in) {
        ShoppingListEntry newEntry = new ShoppingListEntry(URLHandler.decodeString(shoppingListJsData_in.getName()), shoppingListJsData_in.getQuantity());
        newEntry.setIsInShoppingCart(shoppingListJsData_in.getIsInShoppingCart());
        return newEntry;
    }

    /**
     * Converts a JSON string to an array of JS objects
     * @param jsonString_in the string to be converted
     * @return the converted array
     */
    private final native JsArray<ShoppingListJsObject> convertJsonToShoppingListJsData(String jsonString_in);
}
