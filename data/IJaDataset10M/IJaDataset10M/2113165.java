package net.sourceforge.comeback.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sourceforge.jrefactory.ast.Scope;
import org.objectweb.asm.Type;

/**
 * Implements a scope for name resolution.
 *
 * @author Michael Rudolf
 */
public class ScopeImpl implements Scope {

    /**
     * The parent scope or <code>null</code>, if this scope has no parent scope.
     */
    private Scope parent;

    /**
     * Maps names to their corresponding type resolvers in this scope. This 
     * field can be <code>null</code>, if no type resolver has been added to 
     * this scope.
     */
    private Map<String, TypeResolver> typeResolvers;

    /**
     * Maps names to their corresponding variable resolvers in this scope. This 
     * field can be <code>null</code>, if no variable resolver has been added to 
     * this scope.
     */
    private Map<String, VariableResolver> variableResolvers;

    /**
     * Maps names to their corresponding method resolvers in this scope. This 
     * field can be <code>null</code>, if no method resolver has been added to 
     * this scope.
     */
    private Map<String, List<MethodResolver>> methodResolvers;

    /**
     * Maps single-type imports to their corresponding type resolvers in this
     * scope. This field can be <code>null</code>, if no such import statement
     * has been encountered.
     */
    private Map<String, TypeResolver> singleTypeImports;

    /**
     * Maps single static imports to the type resolvers of the corresponding 
     * declaring types in this scope. This field can be <code>null</code>, if 
     * no such import statement has been encountered.
     */
    private Map<String, TypeResolver> singleStaticImports;

    /**
     * Lists on-demand type imports. This field can be <code>null</code>, if no 
     * such import statement has been encountered.
     */
    private List<String> typeImportsOnDemand;

    /**
     * Lists on-demand static imports. This field can be <code>null</code>, if 
     * no such import statement has been encountered.
     */
    private List<String> staticImportsOnDemand;

    public void setParent(Scope parent) {
        this.parent = parent;
    }

    public Scope getParent() {
        return parent;
    }

    /**
     * Adds the given type resolver for the given name to this scope. The 
     * resolver will be accessible in child scopes but not in the parent scopes.
     * 
     * @param name      the name resolved by the given type resolver
     * @param resolver  the type resolver to add
     */
    public void addTypeResolver(String name, TypeResolver resolver) {
        if (typeResolvers == null) {
            typeResolvers = new HashMap<String, TypeResolver>();
        }
        typeResolvers.put(name, resolver);
    }

    /**
     * Resolves the type with the given name in the current scope or delegates
     * to the parent scope, if the resolution failed. If this scope does not 
     * have a parent scope, an exception is thrown.
     * 
     * @param name the name of the type to resolve
     * @return a resolver object for the type with the given name
     */
    public TypeResolver resolveType(String name) {
        TypeResolver resolver = typeResolvers == null ? null : typeResolvers.get(name);
        if (resolver == null) {
            if (parent == null) {
                resolver = singleTypeImports.get(name);
                if (resolver == null) {
                    resolver = singleStaticImports.get(name);
                    if (resolver == null) {
                        Iterator<String> iterator = typeImportsOnDemand.iterator();
                        while (iterator.hasNext()) {
                            String packageName = iterator.next();
                            try {
                                Class<?> clazz = Class.forName(packageName + '.' + name);
                                resolver = new TypeResolver(clazz);
                                break;
                            } catch (ClassNotFoundException ex) {
                            }
                        }
                        if (resolver == null) {
                            iterator = staticImportsOnDemand.iterator();
                            while (iterator.hasNext()) {
                                String className = iterator.next();
                                try {
                                    Class<?> clazz = Class.forName(className);
                                    TypeResolver temp = new TypeResolver(clazz);
                                    resolver = temp.resolveType(name);
                                    break;
                                } catch (ClassNotFoundException ex) {
                                }
                            }
                        }
                    }
                }
                if (resolver == null) {
                    throw new UnresolvableNameException(name);
                }
            } else {
                resolver = ((ScopeImpl) parent).resolveType(name);
            }
        }
        return resolver;
    }

    /**
     * Adds the given variable resolver for the given name to this scope. The 
     * resolver will be accessible in child scopes but not in the parent scopes.
     * 
     * @param name      the name resolved by the given variable resolver
     * @param resolver  the variable resolver to add
     */
    public void addVariableResolver(String name, VariableResolver resolver) {
        if (variableResolvers == null) {
            variableResolvers = new HashMap<String, VariableResolver>();
        }
        variableResolvers.put(name, resolver);
    }

    /**
     * Resolves the local variable or field with the given name in the current 
     * scope or delegates to the parent scope, if the resolution failed. If 
     * this scope does not have a parent scope, an exception is thrown.
     * 
     * @param name the name of the local variable or field to resolve
     * @return  a resolver object for the local variable or field with the 
     *          given name
     */
    public VariableResolver resolveVariable(String name) {
        VariableResolver resolver = variableResolvers == null ? null : variableResolvers.get(name);
        if (resolver == null) {
            if (parent == null) {
                TypeResolver temp = singleStaticImports.get(name);
                if (temp != null) {
                    resolver = temp.resolveVariable(name);
                }
                if (resolver == null) {
                    Iterator<String> iterator = staticImportsOnDemand.iterator();
                    while (iterator.hasNext()) {
                        String className = iterator.next();
                        try {
                            Class<?> clazz = Class.forName(className);
                            temp = new TypeResolver(clazz);
                            resolver = temp.resolveVariable(name);
                            break;
                        } catch (ClassNotFoundException ex) {
                        }
                    }
                }
                if (resolver == null) {
                    throw new UnresolvableNameException(name);
                }
            } else {
                resolver = ((ScopeImpl) parent).resolveVariable(name);
            }
        }
        return resolver;
    }

    /**
     * Adds the given method resolver for the given name to this scope. The 
     * resolver will be accessible in child scopes but not in the parent scopes.
     * 
     * @param name      the name resolved by the given method resolver
     * @param resolver  the method resolver to add
     */
    public void addMethodResolver(String name, MethodResolver resolver) {
        if (methodResolvers == null) {
            methodResolvers = new HashMap<String, List<MethodResolver>>();
        }
        List<MethodResolver> resolvers = methodResolvers.get(name);
        if (resolvers == null) {
            resolvers = new ArrayList<MethodResolver>();
            methodResolvers.put(name, resolvers);
        }
        resolvers.add(resolver);
    }

    /**
     * Resolves the method with the given name and formal parameters matching 
     * the given argument types in the current scope or delegates to the parent 
     * scope, if the resolution failed. If this scope does not have a parent 
     * scope, an exception is thrown.
     * 
     * @param name          the name of the method to resolve
     * @param argumentTypes the argument types for the method invocation
     * @return a resolver object for the method with the given name
     */
    public MethodResolver resolveMethod(String name, Type[] argumentTypes) {
        List<MethodResolver> resolvers = methodResolvers == null ? null : methodResolvers.get(name);
        MethodResolver resolver = null;
        if (resolvers != null) {
            resolver = MethodResolver.findMethodResolver(resolvers, argumentTypes);
        }
        if (resolver == null) {
            if (parent == null) {
                TypeResolver temp = singleStaticImports.get(name);
                if (temp != null) {
                    resolver = temp.resolveMethod(name, argumentTypes);
                }
                if (resolver == null) {
                    Iterator<String> iterator = staticImportsOnDemand.iterator();
                    while (iterator.hasNext()) {
                        String className = iterator.next();
                        try {
                            Class<?> clazz = Class.forName(className);
                            temp = new TypeResolver(clazz);
                            resolver = temp.resolveMethod(name, argumentTypes);
                            break;
                        } catch (ClassNotFoundException ex) {
                        }
                    }
                }
                if (resolver == null) {
                    throw new UnresolvableNameException(name);
                }
            } else {
                resolver = ((ScopeImpl) parent).resolveMethod(name, argumentTypes);
            }
        }
        return resolver;
    }

    /**
     * Adds the given on-demand type import to this scope.
     * 
     * @param type the on-demand type import
     */
    public void addTypeImportOnDemand(String type) {
        if (typeImportsOnDemand == null) {
            typeImportsOnDemand = new ArrayList<String>();
        }
        typeImportsOnDemand.add(type);
    }

    /**
     * Adds the given on-demand static import to this scope.
     * 
     * @param staticImport the on-demand static import
     */
    public void addStaticImportOnDemand(String staticImport) {
        if (staticImportsOnDemand == null) {
            staticImportsOnDemand = new ArrayList<String>();
        }
        staticImportsOnDemand.add(staticImport);
    }

    /**
     * Adds the given single type import to this scope.
     * 
     * @param type the single type to import
     */
    public void addSingleTypeImport(String type) {
        if (singleTypeImports == null) {
            singleTypeImports = new HashMap<String, TypeResolver>();
        }
        TypeResolver resolver;
        try {
            resolver = new TypeResolver(Class.forName(type));
        } catch (Exception ex) {
            throw new UnresolvableNameException(type);
        }
        singleTypeImports.put(type, resolver);
    }

    /**
     * Adds the given single static import to this scope.
     * 
     * @param staticImport the static member to import
     */
    public void addSingleStaticImport(String staticImport) {
        if (singleStaticImports == null) {
            singleStaticImports = new HashMap<String, TypeResolver>();
        }
        int lastDot = staticImport.lastIndexOf('.');
        String type = staticImport.substring(0, lastDot);
        TypeResolver resolver;
        try {
            resolver = new TypeResolver(Class.forName(type));
        } catch (Exception ex) {
            throw new UnresolvableNameException(type);
        }
        singleStaticImports.put(staticImport.substring(lastDot) + 1, resolver);
    }
}
