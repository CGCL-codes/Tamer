/* 
 * OCL validator framework for models and MOF compliant metamodels.
 * Copyright (C) 2004  Fadi Chabarek <fadi.chabarek@web.de>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package de.tuberlin.cs.cis.ocl.type.test;

import junit.framework.TestCase;
import de.tuberlin.cs.cis.ocl.model.check.ClassifierDescriptor;
import de.tuberlin.cs.cis.ocl.model.check.TypedAssociation;
import de.tuberlin.cs.cis.ocl.model.check.TypedProperty;
import de.tuberlin.cs.cis.ocl.type.Classifier;
import de.tuberlin.cs.cis.ocl.type.ModelType;
import de.tuberlin.cs.cis.ocl.type.Property;
import de.tuberlin.cs.cis.ocl.type.Type;
import de.tuberlin.cs.cis.ocl.type.UndefinedFeatureException;

/**
 * Testcase testing the class de.tuberlin.cs.cis.eve.ocl.check.ModelType.   
 * 
 * @author fchabar
 *
 */
public class TestModelType extends TestCase {

	// desc defines no operation, but any attribute or association.
	private final class NoOpAnyAttDesc extends DescAdapter {

		public ClassifierDescriptor getReturnType(
			String featureName,
			Type[] params)
			throws UndefinedFeatureException {
			throw new UndefinedFeatureException();
		}

		public TypedAssociation navigateAssociation(
			String featureName,
			Type[] assocQualifiers)
			throws UndefinedFeatureException {
			return this;
		}
	}

	/**
	 * Constructor for TestModelType.
	 * @param arg0
	 */
	public TestModelType(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(TestModelType.class);
	}

	public void testConforms() throws UndefinedFeatureException {

		// test default conformance
		ClassifierDescriptor desc = new DescAdapter();

		ModelType oclAny = new ModelType(desc);
		assertTrue(oclAny.conforms(Type.AnOclAny));

		// test conformance to integer
		desc = new DescAdapter() {

			public Classifier getOclSupertype() {
				return Classifier.Integer;
			}
		};

		ModelType integer = new ModelType(desc);

		assertTrue(integer.conforms(Type.AnInteger));

		// test conformance to enumeration
		desc = new DescAdapter() {

			public Classifier getOclSupertype() {
				return Classifier.Enumeration;
			}
		};

		ModelType enum = new ModelType(desc);

		assertTrue(enum.conforms(Type.AnEnumeration));

		// test conformance to collection types
		desc = new DescAdapter() {

			public Classifier getOclSupertype() {
				return Classifier.Sequence(Classifier.OclAny);
			}
		};

		ModelType seqAnyType = new ModelType(desc);

		assertTrue(seqAnyType.conforms(Type.ASequence(Type.AnOclAny)));

		// test conformance to other model type
		desc = new DescAdapter() {

			public boolean isSubtypeOf(ClassifierDescriptor mtd) {
				return true;
			}

			public boolean equals(Object o) {
				return false;
			}
		};

		ModelType confModelType = new ModelType(desc);
		assertTrue(confModelType.conforms(confModelType));

		desc = new DescAdapter() {

			public boolean isSubtypeOf(ClassifierDescriptor mtd) {
				return false;
			}

			public boolean equals(Object o) {
				return false;
			}
		};

		ModelType notConfModelType = new ModelType(desc);
		assertTrue(!notConfModelType.conforms(confModelType));
	}

	public void testEquals() {
		ClassifierDescriptor desc = new DescAdapter() {

			public boolean equals(Object modelType) {
				return false;
			}
		};

		ModelType type = new ModelType(desc);

		assertTrue(!type.equals(type));
	}

	public void testGetStructuralType() throws UndefinedFeatureException {

		// test model structural type
		ModelType type = new ModelType(new DescAdapter());
		type.getStructuralFeature("descAdapter", null);

		// test inherited structural type from predefined types
		// uses shorthand collect
		ClassifierDescriptor desc = new DescAdapter() {
			public Classifier getOclSupertype() {
				return Classifier.Bag(
					Classifier.OclType(new ModelType(new NoOpAnyAttDesc())));
			}

			public TypedAssociation navigateAssociation(
				String featureName,
				Type[] params)
				throws UndefinedFeatureException {
				throw new UndefinedFeatureException();
			}
		};

		type = new ModelType(desc);
		type.getStructuralFeature("anyAtt", null);

	}

	public void testWithMalformedDescriptor() throws UndefinedFeatureException {
		// Model information described by third parties can be malformed. The
		// parser must be able to handle these descriptions, without 
		// throwing unintentional runtime exceptions. Warning mechanisms
		// are not tested by this class. 

		// description = null is handled within OclContextChecker

		// test with conformence = null
		ClassifierDescriptor desc = new DescAdapter() {
			public Classifier getOclSupertype() {
				return null;
			}

			public TypedProperty getOperation(
				String featureName,
				ClassifierDescriptor[] params,
				boolean classScoped)
				throws UndefinedFeatureException {
				throw new UndefinedFeatureException();
			}
		};

		ModelType malType = new ModelType(desc);

		Type[] p = { Classifier.OclType };

		Property property = malType.getOperation("oclIsTypeOf", p);
		Type returnType = property.getType();
		assertTrue(returnType.equals(Type.ABoolean));

		// test get feature with null
		desc = new DescAdapter() {

			public TypedProperty getOperation(
				String featureName,
				ClassifierDescriptor[] params,
				boolean classScoped)
				throws UndefinedFeatureException {
				return null;
			}
		};

		malType = new ModelType(desc);
		try {
			malType.getOperation("modelOperation", null);
			fail("test get operational feature with null");
		} catch (UndefinedFeatureException e) {}

		desc = new DescAdapter() {

			public TypedAssociation getAssociation(String featureName, Type[] params)
				throws UndefinedFeatureException {
				return null;
			}

			public TypedProperty getAttribute(
				String featureName,
				boolean classScoped)
				throws UndefinedFeatureException {
				return null;
			}
		};

		malType = new ModelType(desc);
		try {
			malType.getStructuralFeature("modelFeature", null);
			fail("test get structural feature with null");
		} catch (UndefinedFeatureException e) {}

		// test toString = null
		desc = new DescAdapter() {
			public String toString() {
				return null;
			}
		};

		malType = new ModelType(desc);
		assertNotNull(malType.toString());

	}
}
