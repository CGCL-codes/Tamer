package org.vardb.web.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.vardb.CConstants;
import org.vardb.lists.dao.CCart;
import org.vardb.resources.dao.CConfiguration;
import org.vardb.tags.dao.CAttributeDefinition;
import org.vardb.tags.dao.CBundle;
import org.vardb.users.CUserDetails;

public class CExplorerJson extends CAbstractJson {

    protected CUserDetailsJson userdetails;

    protected CConfiguration configuration;

    protected List<Cart> carts;

    protected List<Bundle> bundles;

    public CExplorerJson(CUserDetails user, Collection<CCart> carts, Collection<CBundle> bundles, CConfiguration configuration) {
        this.userdetails = new CUserDetailsJson(user);
        this.configuration = configuration;
        this.carts = convertCarts(carts);
        this.bundles = convertBundles(bundles);
    }

    private List<Bundle> convertBundles(Collection<CBundle> bundles) {
        List<Bundle> list = new ArrayList<Bundle>();
        for (CBundle bundle : bundles) {
            list.add(new Bundle(bundle));
        }
        return list;
    }

    public static class Bundle {

        protected Integer bundle_id;

        protected String name;

        protected List<Definition> definitions = new ArrayList<Definition>();

        public Bundle(CBundle bundle) {
            this.bundle_id = bundle.getId();
            this.name = bundle.getName();
            for (CAttributeDefinition definition : bundle.getDefinitions()) {
                this.definitions.add(new Definition(definition));
            }
        }
    }

    public static class Definition {

        protected Integer id;

        protected String name;

        protected CConstants.DataType type;

        protected String description;

        public Definition(CAttributeDefinition definition) {
            this.id = definition.getId();
            this.name = definition.getName();
            this.type = definition.getType();
            this.description = definition.getDescription();
        }
    }
}
