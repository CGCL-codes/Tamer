package com.triplea.rolap.apitest;

import org.apache.log4j.Logger;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;

public class CellValuesHandlerTest extends BaseTest {

    private static Logger log = Logger.getLogger(CellValuesHandlerTest.class.getName());

    public boolean makeTest(String sid) throws Exception {
        boolean testResult = true;
        String new_name = MainTest.generateRandomName(DUMMY_NAME_LEN);
        int database = createAuxDatabase(new_name, sid);
        int dim_count = 2;
        String dim_names[] = new String[dim_count];
        int dim_dimension[] = new int[dim_count];
        for (int i = 0; i < dim_count; i++) {
            dim_names[i] = MainTest.generateRandomName(DUMMY_NAME_LEN);
            dim_dimension[i] = createAuxDimension(database, dim_names[i], sid);
        }
        HashMap<String, Object> elemA = createElement(database, dim_dimension[0], MainTest.generateRandomName(DUMMY_NAME_LEN), 1, "", "", sid);
        HashMap<String, Object> elemB = createElement(database, dim_dimension[0], MainTest.generateRandomName(DUMMY_NAME_LEN), 1, "", "", sid);
        HashMap<String, Object> elem1 = createElement(database, dim_dimension[0], MainTest.generateRandomName(DUMMY_NAME_LEN), 4, elemA.get("element") + "," + elemB.get("element"), "10,2", sid);
        HashMap<String, Object> elem2 = createElement(database, dim_dimension[0], MainTest.generateRandomName(DUMMY_NAME_LEN), 4, elemA.get("element") + "," + elemB.get("element"), "3,4", sid);
        HashMap<String, Object> elemRoot1 = createElement(database, dim_dimension[0], MainTest.generateRandomName(DUMMY_NAME_LEN), 4, elem1.get("element") + "," + elem2.get("element"), "2,3", sid);
        HashMap<String, Object> elemRoot2 = createElement(database, dim_dimension[1], MainTest.generateRandomName(DUMMY_NAME_LEN), 1, "", "", sid);
        Vector<String> cubeNames = new Vector<String>();
        Vector<Integer> cube_cube = new Vector<Integer>();
        for (int i = 0; i < dim_count - 1; i++) {
            for (int j = i + 1; j < dim_count; j++) {
                String name = MainTest.generateRandomName(DUMMY_NAME_LEN);
                cubeNames.add(name);
                ArrayList<Integer> dims = new ArrayList<Integer>();
                dims.add(dim_dimension[i]);
                dims.add(dim_dimension[j]);
                int cube = createAuxCube(database, name, dims, sid);
                cube_cube.add(cube);
            }
        }
        int cubeIndex = 0;
        for (int i = 0; i < dim_count - 1; i++) {
            for (int j = i + 1; j < dim_count; j++) {
                ArrayList<Integer> path = new ArrayList<Integer>();
                path.add((Integer) elemA.get("element"));
                path.add((Integer) elemRoot2.get("element"));
                auxCellReplace(database, cube_cube.get(cubeIndex), path, 0, "1", sid);
                path.set(0, (Integer) elemB.get("element"));
                path.set(1, (Integer) elemRoot2.get("element"));
                auxCellReplace(database, cube_cube.get(cubeIndex), path, 0, "1", sid);
                cubeIndex++;
            }
        }
        try {
            cubeIndex = 0;
            for (int i = 0; i < dim_count - 1; i++) {
                for (int j = i + 1; j < dim_count; j++) {
                    ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
                    ArrayList<Integer> path1 = new ArrayList<Integer>();
                    path1.add((Integer) elemRoot1.get("element"));
                    path1.add((Integer) elemRoot2.get("element"));
                    paths.add(path1);
                    ArrayList<Integer> path2 = new ArrayList<Integer>();
                    path2.add((Integer) elem1.get("element"));
                    path2.add((Integer) elemRoot2.get("element"));
                    paths.add(path2);
                    ArrayList<Integer> path3 = new ArrayList<Integer>();
                    path3.add((Integer) elem2.get("element"));
                    path3.add((Integer) elemRoot2.get("element"));
                    paths.add(path3);
                    ArrayList<ArrayList<String>> values = auxGetCellValues(database, cube_cube.get(cubeIndex), paths, sid);
                    String val1 = values.get(0).get(2);
                    if (!val1.equals("45.0")) testResult = false;
                    String val2 = values.get(1).get(2);
                    if (!val2.equals("12.0")) testResult = false;
                    String val3 = values.get(2).get(2);
                    if (!val3.equals("7.0")) testResult = false;
                    cubeIndex++;
                }
            }
        } catch (Exception e) {
            log.error("Error in test core: " + e.getMessage());
            testResult = false;
        }
        for (int cube : cube_cube) destroyAuxCube(database, cube, sid);
        for (int dim : dim_dimension) destroyAuxDimension(database, dim, sid);
        destroyAuxDdatabase(database, sid);
        return testResult;
    }
}
