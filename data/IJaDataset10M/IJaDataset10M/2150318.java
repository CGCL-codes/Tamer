package moten.david.xuml.model.mellor;

import java.io.File;
import model.AssociationClass;
import model.CallEvent;
import model.Class;
import model.Package;
import model.Primitive;
import model.SignalEvent;
import model.State;
import model.TimerEvent;
import moten.david.xuml.model.Generator;
import moten.david.xuml.model.Multiplicity;
import moten.david.xuml.model.compiler.Compiler;
import moten.david.xuml.model.compiler.util.Util;
import moten.david.xuml.model.util.SystemBase;

public class Bookstore extends SystemBase {

    public Bookstore() {
        super("BOOKSTORE", "Bookstore");
        initialize();
    }

    private void initialize() {
        model.Package pkg = createRootPackage("bookstore", "an online bookstore");
        Class book = createBook(pkg);
        Class author = createAuthor(pkg);
        Class publisher = createPublisher(pkg);
        AssociationClass authorship = createAuthorship(pkg);
        AssociationClass productSelection = createProductSelection(pkg);
        Class shipment = createShipment(pkg);
        Class order = createOrder(pkg);
        Class customer = createCustomer(pkg);
        Class creditCardCharge = createCreditCardCharge(pkg);
        createAssociation("R5", createAssociationEndPrimary(order, Multiplicity.ONE_MANY, "places"), createAssociationEndSecondary(customer, Multiplicity.ZERO_ONE, "is placed by"));
        createAssociation("R6", createAssociationEndPrimary(order, Multiplicity.ONE, "delivers contents of"), createAssociationEndSecondary(shipment, Multiplicity.ZERO_ONE, "is sent to customer as"));
        createAssociation("R7", createAssociationEndPrimary(order, Multiplicity.ONE, "pays for"), createAssociationEndSecondary(creditCardCharge, "creditCardCharge", Multiplicity.ZERO_ONE, "is paid for by"));
        createAssociation("R1", createAssociationEndPrimary(book, Multiplicity.MANY, "produces and markets"), createAssociationEndSecondary(publisher, Multiplicity.ONE, "is produced and marketed by"));
        createAssociation("R4", createAssociationEndPrimary(book, Multiplicity.ONE_MANY, "contains"), createAssociationEndSecondary(order, Multiplicity.MANY, "is purchased on")).setAssociationClass(productSelection);
        createAssociation("R2", createAssociationEndPrimary(author, Multiplicity.ONE_MANY, "is written by"), createAssociationEndSecondary(book, Multiplicity.MANY, "wrote")).setAssociationClass(authorship);
    }

    private Class createCreditCardCharge(Package pkg) {
        Class credit = createClass(pkg, "CreditCardCharge", "a charge made to a credit card");
        createIdentifierPrimary(createAttribute(credit, "chargeId", Primitive.ARBITRARY_ID), Generator.GENERATED_VALUE);
        createAttribute(credit, "accountNumber");
        createAttribute(credit, "cardholderName");
        createAttribute(credit, "billingAddress");
        createAttribute(credit, "cardExpirationDate", Primitive.DATE);
        createAttribute(credit, "dateChargeMade", Primitive.DATE);
        createAttribute(credit, "chargeAmount", Primitive.DECIMAL);
        createAttribute(credit, "bankApprovalCode");
        return credit;
    }

    private Class createCustomer(Package pkg) {
        Class customer = createClass(pkg, "Customer", "someone that orders books");
        createIdentifierPrimary(createAttribute(customer, "email"), Generator.NOT_GENERATED);
        createAttribute(customer, "name");
        createAttribute(customer, "shippingAddress");
        createAttribute(customer, "phone");
        createAttribute(customer, "purchasesMade");
        return customer;
    }

    private Class createOrder(Package pkg) {
        Class order = createClass(pkg, "Order", "a customer's order of books");
        createIdentifierPrimary(createAttribute(order, "id", Primitive.ARBITRARY_ID), Generator.GENERATED_VALUE);
        createAttribute(order, "dateOrderPlaced", Primitive.DATE);
        createAttribute(order, "recipient");
        createAttribute(order, "deliveryAddress");
        createAttribute(order, "contactPhone");
        State open = createState(order, "Open");
        State delivered = createState(order, "Delivered");
        State cancelled = createState(order, "Cancelled");
        SignalEvent orderDelivered = createSignalEvent(order, "orderDelivered");
        CallEvent orderCancelled = createCallEvent(order, "orderCancelled");
        createParameter(orderCancelled, "reason");
        TimerEvent orderTimeout = createTimerEvent(order, "orderTimeout");
        createTransition(open, delivered, orderDelivered);
        createTransition(open, cancelled, orderCancelled);
        createTransition(open, cancelled, orderTimeout);
        return order;
    }

    private Class createShipment(Package pkg) {
        Class shipment = createClass(pkg, "Shipment", "the sending of an order to a customer");
        createIdentifierPrimary(createAttribute(shipment, "id", Primitive.ARBITRARY_ID), Generator.GENERATED_VALUE);
        createAttribute(shipment, "trackingNumber");
        createAttribute(shipment, "recipient");
        createAttribute(shipment, "deliveryAddress");
        createAttribute(shipment, "contactPhone");
        createAttribute(shipment, "timePrepared", Primitive.DATE);
        createAttribute(shipment, "timePickedUp", Primitive.DATE);
        createAttribute(shipment, "timeDelivered", Primitive.DATE);
        return shipment;
    }

    private AssociationClass createProductSelection(Package pkg) {
        AssociationClass a = createAssociationClass(pkg, "ProductSelection", "what books are on what orders");
        createIdentifierPrimary(createAttribute(a, "id", Primitive.ARBITRARY_ID), Generator.GENERATED_VALUE);
        createAttribute(a, "quantity", Primitive.INTEGER);
        createAttribute(a, "unitPriceOfSelection", Primitive.DECIMAL);
        return a;
    }

    private AssociationClass createAuthorship(Package pkg) {
        AssociationClass a = createAssociationClassWithArbitraryId(pkg, "Authorship", "multiple authors of one book");
        createAttribute(a, "preCredit").setMandatory(false);
        createAttribute(a, "postCredit").setMandatory(false);
        return a;
    }

    private Class createPublisher(Package pkg) {
        Class publisher = createClass(pkg, "Publisher", "the publisher of a book, volume or journal");
        createIdentifierPrimary(createAttribute(publisher, "id", Primitive.ARBITRARY_ID), Generator.GENERATED_VALUE);
        createIdentifierNonPrimary(publisher, createAttribute(publisher, "groupCode"), createAttribute(publisher, "publisherCode"));
        createAttribute(publisher, "name");
        createAttribute(publisher, "address").setMandatory(false);
        createAttribute(publisher, "website").setMandatory(false);
        return publisher;
    }

    private Class createAuthor(Package pkg) {
        Class author = createClass(pkg, "Author", "the author of a book, volume or journal");
        createIdentifierPrimary(createAttribute(author, "name"), Generator.NOT_GENERATED);
        createAttribute(author, "website").setMandatory(false);
        createAttribute(author, "email").setMandatory(false);
        return author;
    }

    private Class createBook(Package pkg) {
        Class book = createClass(pkg, "Book", "a book, volume or journal");
        createIdentifierPrimary(createAttribute(book, "bookNumber"), Generator.NOT_GENERATED);
        createAttribute(book, "productId");
        createAttribute(book, "title");
        createAttribute(book, "subtitle").setMandatory(false);
        createAttribute(book, "copyrightYear", Primitive.INTEGER).setMandatory(false);
        createAttribute(book, "unitPrice", Primitive.DECIMAL).setMandatory(false);
        createAttribute(book, "description").setMandatory(false);
        createAttribute(book, "webSiteUrl").setMandatory(false);
        return book;
    }

    @Override
    public void generate(String outputDirectoryName, String resourcesDirectoryName, String webDirectoryName, String docsDirectoryName) throws Exception {
        File outputDirectory = new File(outputDirectoryName);
        Util.delete(outputDirectory);
        outputDirectory.mkdirs();
        Compiler compiler = new Compiler(getSystem(), outputDirectory, outputDirectory, outputDirectory, outputDirectory);
        compiler.compile();
    }
}
