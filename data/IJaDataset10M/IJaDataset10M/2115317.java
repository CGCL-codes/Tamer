package com.luis.db.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.luis.db.android.controller.CategoryController;
import com.luis.db.android.util.DetailsController;
import com.luis.db.android.controller.EnterpriseController;
import com.luis.db.android.controller.OrderController;
import com.luis.db.android.controller.PackageController;
import com.luis.db.android.controller.ProductController;
import com.luis.db.android.controller.ProductOrderController;
import com.luis.db.android.controller.ProductPackageController;
import com.luis.db.android.controller.RoleController;
import com.luis.db.android.controller.UserController;
import java.io.Serializable;

/**
 *
 * @author luis
 */
public class EntityDetails extends Activity {

    protected static final int ACTIVITY_ID = 0x600;

    private static final int CREATE = Menu.FIRST;

    private static final int UPDATE = Menu.FIRST + 1;

    private static final int REMOVE = Menu.FIRST + 2;

    private static final int BACK = Menu.FIRST + 3;

    private static final int IMPORT = Menu.FIRST + 4;

    private static final int HOME = Menu.FIRST + 5;

    private static final int EXIT = Menu.FIRST + 6;

    private String service;

    private int id;

    private int step;

    private String code;

    private String server;

    private String role;

    private String side;

    private String supplier;

    private Serializable retrieved;

    private boolean imported;

    private DetailsController controller;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Bundle data = getIntent().getExtras();
        service = data.getString("SERVICE");
        server = data.getString("SERVER_ADDR");
        role = data.getString("USER_ROLE");
        side = data.getString("APP_SIDE");
        supplier = data.getString("SUPPLIER_ADDR");
        imported = data.getBoolean("IMPORTED");
        step = data.getInt("STEP");
        if (step == 1) {
            retrieved = data.getSerializable("RETRIEVED");
        } else {
            retrieved = null;
        }
        if (imported && step == 0) {
            setTitle(side + " " + service.replace("_", " ") + " IMPORT");
        } else {
            setTitle(side + " " + service.replace("_", " ") + " DETAILS");
        }
        id = data.getInt("ID");
        if (id == -1) {
            setView();
        } else {
            loadData();
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle data = new Bundle();
        data.putString("CODE", code);
        intent.putExtras(data);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private void setView() {
        String prefix = "Client";
        if (side.equals("SUPPLIER")) {
            prefix = "Supplier";
        }
        if (service.equals("ENTERPRISE")) {
            controller = new EnterpriseController(prefix, server);
        }
        if (service.equals("CATEGORY")) {
            controller = new CategoryController(prefix, server);
        }
        if (service.equals("ORDER")) {
            String addr = server;
            if (side.equals("CLIENT")) {
                addr = supplier;
                prefix = "Supplier";
            }
            controller = new OrderController(prefix, addr);
        }
        if (service.equals("PACKAGE_BY_ORDER")) {
            String addr = server;
            if (side.equals("CLIENT")) {
                addr = supplier;
                prefix = "Supplier";
            }
            controller = new PackageController(prefix, addr);
        }
        if (service.equals("PRODUCT")) {
            if (step == 1 || (imported && step == 0)) {
                if (step == 1) {
                    controller = new ProductController("Client", server);
                    controller.setRetrieved(retrieved);
                } else {
                    controller = new ProductController("Supplier", supplier);
                }
                controller.setImport(imported);
            } else {
                controller = new ProductController(prefix, server);
            }
        }
        if (service.equals("USER")) {
            controller = new UserController(prefix, server);
        }
        if (service.equals("ROLE")) {
            controller = new RoleController(prefix, server);
        }
        if (service.equals("PRODUCT_BY_ORDER")) {
            String addr = server;
            if (side.equals("CLIENT")) {
                addr = supplier;
                prefix = "Supplier";
            }
            controller = new ProductOrderController(prefix, addr);
        }
        if (service.equals("PRODUCT_BY_PACKAGE")) {
            String addr = server;
            if (side.equals("CLIENT")) {
                addr = supplier;
                prefix = "Supplier";
            }
            controller = new ProductPackageController(prefix, addr);
        }
        controller.setActivity(this);
        controller.setView();
    }

    private void loadData() {
        setView();
        try {
            controller.loadData(id);
        } catch (Exception e) {
            Toast error = Toast.makeText(getApplicationContext(), "ERROR: " + e.toString(), Toast.LENGTH_LONG);
            error.show();
        }
    }

    @Override
    public void onBackPressed() {
        code = "BACK";
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (id == -1 || step == 1) {
            if (imported && step == 0) {
                menu.addSubMenu(Menu.NONE, IMPORT, 0, "Retrieve");
                menu.addSubMenu(Menu.NONE, BACK, 1, "Cancel");
            } else {
                menu.addSubMenu(Menu.NONE, CREATE, 0, "Save");
                menu.addSubMenu(Menu.NONE, BACK, 1, "Cancel");
            }
        } else {
            if ((service.equals("PACKAGE_BY_ORDER") && side.equals("CLIENT")) || service.equals("PRODUCT_BY_PACKAGE") || service.equals("PRODUCT_BY_ORDER")) {
                menu.addSubMenu(Menu.NONE, BACK, 0, "Back");
                menu.addSubMenu(Menu.NONE, HOME, 1, "Home");
            } else {
                if (role.equals("admin")) {
                    menu.addSubMenu(Menu.NONE, UPDATE, 0, "Update");
                    menu.addSubMenu(Menu.NONE, REMOVE, 1, "Remove");
                    menu.addSubMenu(Menu.NONE, BACK, 2, "Back");
                    menu.addSubMenu(Menu.NONE, HOME, 3, "Home");
                } else {
                    menu.addSubMenu(Menu.NONE, BACK, 0, "Back");
                    menu.addSubMenu(Menu.NONE, HOME, 1, "Home");
                }
            }
        }
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        super.onMenuItemSelected(featureId, item);
        code = "BACK";
        switch(item.getItemId()) {
            case CREATE:
                create();
                break;
            case IMPORT:
                retrieve();
                break;
            case UPDATE:
                update();
                break;
            case REMOVE:
                remove();
                break;
            case HOME:
                code = "HOME";
                finish();
            case BACK:
                break;
            case EXIT:
                code = "EXIT";
                break;
        }
        finish();
        return true;
    }

    private void update() {
        String message = service + " ";
        try {
            int row = controller.update(id);
            if (row == -1) {
                message += "NOT ";
            }
            if (id == -1) {
                message += "CREATED";
            } else {
                message += "UPDATED";
            }
        } catch (Exception e) {
            message = "ERROR: " + e.toString();
        }
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void remove() {
        String message = service + " ";
        try {
            int row = controller.delete(id);
            if (row == -1) {
                message += "NOT ";
            }
            message += "REMOVED";
        } catch (Exception e) {
            message = "ERROR: " + e.toString();
        }
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void create() {
        if (id != -1) {
            controller.setServer("Client", server);
            id = -1;
        }
        update();
    }

    private void retrieve() {
        String message = service + " ";
        try {
            retrieved = controller.retrieveData();
            if (retrieved == null) {
                message += "NOT ";
            }
            message += "IMPORTED";
        } catch (Exception e) {
            message = "ERROR: " + e.toString();
        }
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        if (retrieved != null) {
            id = controller.getId(retrieved);
            Intent intent = new Intent(this, EntityDetails.class);
            Bundle data = new Bundle();
            data.putString("SERVICE", service);
            data.putString("SERVER_ADDR", server);
            data.putString("USER_ROLE", role);
            data.putString("SUPPLIER_ADDR", supplier);
            data.putString("APP_SIDE", side);
            data.putInt("STEP", 1);
            data.putBoolean("IMPORTED", false);
            data.putInt("ID", id);
            data.putSerializable("RETRIEVED", (Serializable) retrieved);
            intent.putExtras(data);
            startActivityForResult(intent, ACTIVITY_ID);
        }
    }
}
