package org.sf.autowire;

public class PackageResolverProvider implements TypeResolverSPI {

    public TypeResolver create(String uri) {
        String prefix = "package://";
        if (uri.startsWith(prefix)) {
            return new PackageResolver(uri.substring(prefix.length()));
        }
        return null;
    }

    public static class PackageResolver implements TypeResolver {

        private String pkgName;

        public PackageResolver(String pkgName) {
            this.pkgName = pkgName;
        }

        public Class<?> resolve(Autowire aw, String name) {
            try {
                return aw.getClassLoader().loadClass(pkgName + "." + name);
            } catch (Throwable e) {
                if (Character.isLowerCase(name.charAt(0))) {
                    return resolve(aw, Character.toUpperCase(name.charAt(0)) + name.substring(1));
                }
                return null;
            }
        }
    }
}
