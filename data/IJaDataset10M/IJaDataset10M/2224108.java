package com.google.gwt.ricordo.client;

import java.util.ArrayList;
import java.util.LinkedList;
import com.google.gwt.ricordo.shared.CompositeDetailsLight;
import com.google.gwt.ricordo.shared.ModelDetailsLight;
import com.google.gwt.ricordo.shared.ModelSearch;
import com.google.gwt.ricordo.shared.VariableDetailsLight;
import com.google.gwt.ricordo.shared.VariableSearch;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QueryServiceAsync {

    public void getVariableList(ModelSearch modelSearch, AsyncCallback<ArrayList<VariableDetailsLight>> callback);

    public void getManQueryTypes(AsyncCallback<ArrayList<String>> asyncCallback);

    public void getOntoTerms(String ontoTerm, AsyncCallback<LinkedList<String>> asyncCallback);

    public void setOntoTerms(ModelSearch modelSearch, AsyncCallback<ModelSearch> asyncCallback);

    public void searchModelData(ModelSearch modelSearch, AsyncCallback<ArrayList<ModelDetailsLight>> asyncCallback);

    public void getCompoistes(ModelSearch modelSearch, AsyncCallback<ArrayList<CompositeDetailsLight>> asyncCallback);

    public void getVariableListForAnnotation(VariableSearch variableSearch, AsyncCallback<ArrayList<VariableDetailsLight>> asyncCallback);

    public void getVariableAnnotationList(VariableSearch variableSearch, AsyncCallback<ArrayList<VariableDetailsLight>> asyncCallback);

    public void getMiriamQualifiers(AsyncCallback<ArrayList<String>> callback);

    public void addAnnotation(String variableURL, String property, String annotValue, AsyncCallback<Void> callback);

    public void getTermToAnnotate(String manQuery, AsyncCallback<String> callback);

    public void getRelations(AsyncCallback<ArrayList<String>> asyncCallback);
}
