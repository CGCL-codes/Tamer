package com.triplea.rolap.apitest;

import java.util.Vector;

public class CubeSaveHandlerTest extends BaseTest {

    public boolean makeTest(String sid) throws Exception {
        String new_name = MainTest.generateRandomName(DUMMY_NAME_LEN);
        String query = "/database/create?new_name=" + new_name + "&sid=" + sid;
        Vector<String> res = MainTest.sendRequest(query);
        if (res.size() != 1) {
            return false;
        }
        String vals[] = MainTest.getList(res.get(0), ';');
        if (vals.length != 6) {
            return false;
        }
        int database = Integer.parseInt(vals[0]);
        String name_database = vals[1];
        int number_dimensions = Integer.parseInt(vals[2]);
        int number_cubes = Integer.parseInt(vals[3]);
        int status = Integer.parseInt(vals[4]);
        int type = Integer.parseInt(vals[5]);
        if (!new_name.equals(name_database)) {
            return false;
        }
        if (number_dimensions != 0) {
            return false;
        }
        if (number_cubes != 0) {
            return false;
        }
        if (status != 0 && status != 1 && status != 2) {
            return false;
        }
        if (type != 0 && type != 1) {
            return false;
        }
        int dim_count = 5;
        String dim_names[] = new String[dim_count];
        int dim_dimension[] = new int[dim_count];
        String dim_name_dimension[] = new String[dim_count];
        int dim_number_elements[] = new int[dim_count];
        int dim_maximum_level[] = new int[dim_count];
        int dim_maximum_indent[] = new int[dim_count];
        int dim_maximum_depth[] = new int[dim_count];
        for (int i = 0; i < dim_count; i++) {
            dim_names[i] = MainTest.generateRandomName(DUMMY_NAME_LEN);
            query = "/dimension/create?database=" + database + "&new_name=" + dim_names[i] + "&sid=" + sid;
            res = MainTest.sendRequest(query);
            if (res.size() != 1) {
                return false;
            }
            vals = MainTest.getList(res.get(0), ';');
            if (vals.length != 6) {
                return false;
            }
            dim_dimension[i] = Integer.parseInt(vals[0]);
            dim_name_dimension[i] = vals[1];
            dim_number_elements[i] = Integer.parseInt(vals[2]);
            dim_maximum_level[i] = Integer.parseInt(vals[3]);
            dim_maximum_indent[i] = Integer.parseInt(vals[4]);
            dim_maximum_depth[i] = Integer.parseInt(vals[5]);
            if (!dim_names[i].equals(dim_name_dimension[i])) {
                return false;
            }
        }
        Vector<String> cubeNames = new Vector<String>();
        Vector<Integer> cube_cube = new Vector<Integer>();
        Vector<String> cube_name_cube = new Vector<String>();
        Vector<Integer> cube_number_dimensions = new Vector<Integer>();
        Vector<String> cube_dimensions = new Vector<String>();
        Vector<Integer> cube_number_cells = new Vector<Integer>();
        Vector<Integer> cube_number_filled_cells = new Vector<Integer>();
        Vector<Integer> cube_status = new Vector<Integer>();
        for (int i = 0; i < dim_count - 1; i++) {
            for (int j = i + 1; j < dim_count; j++) {
                String name = MainTest.generateRandomName(DUMMY_NAME_LEN);
                cubeNames.add(name);
                query = "/cube/create?database=" + database + "&new_name=" + name + "&dimensions=" + dim_dimension[i] + "," + dim_dimension[j] + "&sid=" + sid;
                res = MainTest.sendRequest(query);
                if (res.size() != 1) {
                    return false;
                }
                vals = MainTest.getList(res.get(0), ';');
                if (vals.length != 7) {
                    return false;
                }
                cube_cube.add(Integer.parseInt(vals[0]));
                cube_name_cube.add(vals[1]);
                cube_number_dimensions.add(Integer.parseInt(vals[2]));
                cube_dimensions.add(vals[3]);
                cube_number_cells.add(Integer.parseInt(vals[4]));
                cube_number_filled_cells.add(Integer.parseInt(vals[5]));
                cube_status.add(Integer.parseInt(vals[6]));
            }
        }
        int cubeIndex = 0;
        for (int i = 0; i < dim_count - 1; i++) {
            for (int j = i + 1; j < dim_count; j++) {
                query = "/cube/save?database=" + database + "&cube=" + cube_cube.get(cubeIndex) + "&sid=" + sid;
                res = MainTest.sendRequest(query);
                if (res.size() != 1) {
                    return false;
                }
                vals = MainTest.getList(res.get(0), ';');
                if (vals.length != 1) {
                    return false;
                }
                if (!"1".equals(vals[0])) {
                    return false;
                }
                cubeIndex++;
            }
        }
        int counter = 0;
        for (int i = 0; i < dim_count - 1; i++) {
            for (int j = i + 1; j < dim_count; j++) {
                query = "/cube/destroy?database=" + database + "&cube=" + cube_cube.get(counter) + "&sid=" + sid;
                res = MainTest.sendRequest(query);
                if (res.size() != 1) {
                    return false;
                }
                vals = MainTest.getList(res.get(0), ';');
                if (vals.length != 1) {
                    return false;
                }
                counter++;
            }
        }
        query = "/database/destroy?database=" + database + "&sid=" + sid;
        res = MainTest.sendRequest(query);
        if (res.size() != 1) {
            return false;
        }
        vals = MainTest.getList(res.get(0), ';');
        return vals.length == 1 && "1".equals(vals[0]);
    }
}
