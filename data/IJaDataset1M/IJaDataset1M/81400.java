package Converter;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import Gui.BoshiGui;
import Gui.CreateCompanyPanel;
import Gui.ImageInstantPanel;
import Gui.PdfInstantJTabbedPane;
import Gui.PdfInstantPanel;

/**
 * Class to Handle one EML file.
 *
 * @author Garry Chan
 *
 */
public class EMLHandler {

    private static Client currentClient;

    public static void emlParser(File emlFile, String modifiedFolder) {
        try {
            currentClient = ((BoshiGui) BoshiGui.getComponent()).getCurrentClient();
            String fullEmlName = emlFile.getName();
            if (Global.DEBUG) {
                System.out.println("Parsing current EML File Name with Extension: " + fullEmlName);
            }
            int lastIndex = fullEmlName.lastIndexOf('.');
            String emlName = fullEmlName.substring(0, lastIndex);
            if (Global.DEBUG) {
                System.out.println("Parsing current EML name with no Extension: " + emlName);
            }
            if (!getExtension(fullEmlName).equalsIgnoreCase("eml")) {
                return;
            }
            InputStream source = new FileInputStream(emlFile);
            MimeMessage message = new MimeMessage(null, source);
            Address[] addresses = message.getFrom();
            if (addresses.length == 0) return;
            InternetAddress fromAddress = (InternetAddress) addresses[0];
            String emailAdd = fromAddress.getAddress().toLowerCase().trim();
            String domain = getDomainAddress(emailAdd);
            if (domain.equalsIgnoreCase("boshifilter.com")) {
                String subjectContent = message.getSubject();
                StringTokenizer st = new StringTokenizer(subjectContent);
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    if (s.contains("@") && s.contains(".")) {
                        String clientEmail = null;
                        if (s.contains(":")) {
                            clientEmail = s.substring(s.indexOf(":"));
                        } else {
                            clientEmail = s;
                        }
                        emailAdd = clientEmail;
                        break;
                    }
                }
            }
            if (Global.DEBUG) {
                System.out.println("Sender of the eml(" + emlName + ")'s Domain is: " + domain);
            }
            Set<Client> clients = null;
            clients = ((BoshiGui) BoshiGui.getComponent()).getClients();
            boolean foundClient = false;
            boolean gotPopularDomain = false;
            ArrayList<String> popularDomains = new ArrayList<String>();
            for (String s : ((BoshiGui) BoshiGui.getComponent()).getPopularDomains()) {
                popularDomains.add(s);
            }
            for (Client c : clients) {
                List<String> emails = c.getEmails();
                for (String email : emails) {
                    String emailLC = email.toLowerCase().trim();
                    for (String pd : popularDomains) {
                        if (emailAdd.contains(pd)) {
                            gotPopularDomain = true;
                            if (Global.DEBUG) {
                                System.out.println("Client email : " + emailLC + " comparing From Address : " + emailAdd);
                            }
                            if (emailLC.trim().equalsIgnoreCase(emailAdd)) {
                                if (Global.DEBUG) {
                                    System.out.println("Client email : " + emailLC + " = From Address : " + emailAdd);
                                }
                                currentClient = c;
                                foundClient = true;
                                break;
                            }
                        }
                    }
                    if (foundClient) break;
                    if (!gotPopularDomain) {
                        if (emailLC.contains(domain.toLowerCase())) {
                            currentClient = c;
                            foundClient = true;
                            break;
                        }
                    }
                }
                if (foundClient) {
                    break;
                }
            }
            if (!foundClient) {
                if (Global.DEBUG) System.out.println("An existing Client cannot be found and not suitable for the EML(" + fullEmlName + ")");
                JOptionPane.showMessageDialog(BoshiGui.getComponent(), "An existing Client cannot be found and not suitable for the EML(" + fullEmlName + ")");
                String emlCodeNum = editExcelInCoding(emlName.substring(4));
                if (emlCodeNum == null) {
                    JOptionPane.showMessageDialog(BoshiGui.getComponent(), "The xls (EmaiIn) File does not exist or is being used by other programs\n" + "Please Close the excel file before running conversion");
                    return;
                } else if (emlCodeNum.equals("")) {
                    ((BoshiGui) BoshiGui.getComponent()).addUnkownEml(emlName + ": duplicated");
                    message.setSubject(emlName + "-duplicated: (" + message.getSubject() + ")");
                    message.saveChanges();
                    message.writeTo(new FileOutputStream(new File(modifiedFolder + "EML" + File.separator + emlName + "-duplicated.eml")));
                } else {
                    ((BoshiGui) BoshiGui.getComponent()).addUnkownEml(emlName);
                    message.setSubject(emlCodeNum + ": (" + message.getSubject() + ")");
                    message.saveChanges();
                    message.writeTo(new FileOutputStream(new File(modifiedFolder + "EML" + File.separator + emlCodeNum + ".eml")));
                }
                return;
            }
            if (foundClient) {
                String emlCodeNum = editExcelInCoding(emlName.substring(4));
                if (emlCodeNum == null) {
                    JOptionPane.showMessageDialog(BoshiGui.getComponent(), "The xls (EmaiIn) File does not exist or is being used by other programs\n" + "Please Close the excel file before running conversion");
                    return;
                } else if (emlCodeNum.equals("")) {
                    ((BoshiGui) BoshiGui.getComponent()).addUnkownEml(emlName + ": duplicated");
                    message.setSubject(emlName + "-duplicated: (" + message.getSubject() + ")");
                    message.saveChanges();
                    message.writeTo(new FileOutputStream(new File(modifiedFolder + "EML" + File.separator + emlName + "-duplicated.eml")));
                    return;
                }
                if (Global.DEBUG) {
                    System.out.println("Subject: " + message.getSubject());
                    System.out.print("Sent Date: " + message.getSentDate());
                    System.out.println(". From: " + emailAdd);
                }
                Multipart mp = null;
                String contentType = message.getContentType();
                ContentType cType = new ContentType(contentType);
                if (Global.DEBUG) {
                    System.out.println("Content Type is: " + contentType);
                    System.out.println("CType toString: " + cType.toString());
                }
                boolean attachment = false;
                if (cType.match("text/plain")) {
                    System.out.println("EML " + fullEmlName + " has only text/plain.");
                } else if (cType.toString().contains("multipart")) {
                    mp = (Multipart) message.getContent();
                    if (Global.DEBUG) {
                        System.out.println("EML " + fullEmlName + " has " + mp.getCount() + " multiparts");
                    }
                    int mpCount = mp.getCount();
                    ArrayList<BodyPart> partIndexTobeRemoved = new ArrayList<BodyPart>();
                    try {
                        for (int i = 0; i < mpCount; i++) {
                            if (Global.DEBUG) {
                                System.out.println("MultiPart: " + i);
                                System.out.println("Getting Multipart: " + i);
                            }
                            Part p = mp.getBodyPart(i);
                            String disposition = p.getDisposition();
                            if (Global.DEBUG) {
                                System.out.println("Disposition: " + disposition);
                            }
                            if (p.getDisposition() == null) {
                            } else if (p.getDisposition().equals(Part.ATTACHMENT) || p.getDisposition().equals(Part.INLINE)) {
                                attachment = true;
                                MimeBodyPart mbp = (MimeBodyPart) p;
                                String filename = mbp.getFileName();
                                if (Global.DEBUG) {
                                    System.out.println("mbp description: " + mbp.getDescription());
                                    System.out.println("mbp disposition:" + mbp.getDisposition());
                                    System.out.println("Attachment Filename :" + filename);
                                }
                                if (filename == null) {
                                    filename = "noFileName.";
                                }
                                String extension = getExtension(filename);
                                if (Global.DEBUG) {
                                    System.out.println("Attachment Filename extension: " + extension);
                                }
                                String noExtFileName = filename.substring(0, filename.lastIndexOf("."));
                                if (extension == null) {
                                    extension = "";
                                }
                                String newFileName = modifiedFolder + emlCodeNum + "-" + i + "(" + noExtFileName + ")." + extension;
                                String pdfTempFileName = modifiedFolder + emlCodeNum + "-" + i + "(" + noExtFileName + ")-temp." + extension;
                                if (extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")) {
                                    mbp.saveFile(newFileName);
                                    ModifyExcel.excelParserToExcel(currentClient, new File(newFileName));
                                    partIndexTobeRemoved.add(mbp);
                                } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg")) {
                                    String imageFilePath = modifiedFolder + emlCodeNum + "-" + i + ".JPG";
                                    mbp.saveFile(imageFilePath);
                                    if (Global.DEBUG) System.out.println("Looking for any Rectangles....");
                                    if (currentClient.getImageRecs().size() == 0) {
                                        if (Global.DEBUG) System.out.println("No rectangles of Images");
                                        ImageInstantPanel imageip = new ImageInstantPanel(currentClient, imageFilePath);
                                        JOptionPane pane = new JOptionPane(imageip, JOptionPane.YES_NO_CANCEL_OPTION);
                                        Icon icon = new ImageIcon();
                                        pane.setIcon(icon);
                                        JDialog dialog = pane.createDialog(null, "");
                                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                                        Dimension scrnsize = toolkit.getScreenSize();
                                        scrnsize.setSize(scrnsize.getWidth() * 0.9, scrnsize.getHeight() * 0.9);
                                        dialog.setLocation(0, 0);
                                        dialog.setMaximumSize(scrnsize);
                                        dialog.setResizable(true);
                                        dialog.setVisible(true);
                                        dialog.pack();
                                        File f = new File(imageFilePath);
                                        InputStream is = new BufferedInputStream(new FileInputStream(f));
                                        BufferedImage bi = ImageIO.read(is);
                                        BufferedImage newbi = ModifyImage.pdfToImageInstantEdit(imageip.getRect(), bi, currentClient);
                                        is.close();
                                        ImageIO.write(newbi, "JPG", f);
                                    } else {
                                        if (Global.DEBUG) System.out.println("Have Rectangles size for Image: " + currentClient.getPdfRecs().size());
                                        ModifyImage.imageParser(currentClient, new File(imageFilePath));
                                    }
                                    partIndexTobeRemoved.add(mbp);
                                } else if (extension.equalsIgnoreCase("pdf")) {
                                    mbp.saveFile(pdfTempFileName);
                                    if (Global.DEBUG) System.out.println("Looking for any Rectangles....");
                                    Document document = new Document();
                                    try {
                                        document.setFile(pdfTempFileName);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    int pages = document.getNumberOfPages();
                                    if (pages > 1) {
                                        PdfInstantJTabbedPane pdfip = new PdfInstantJTabbedPane(currentClient, pdfTempFileName);
                                        JOptionPane pane = new JOptionPane(pdfip, JOptionPane.YES_NO_CANCEL_OPTION);
                                        Icon icon = new ImageIcon();
                                        pane.setIcon(icon);
                                        JDialog dialog = pane.createDialog(null, pdfTempFileName);
                                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                                        Dimension scrnsize = toolkit.getScreenSize();
                                        scrnsize.setSize(scrnsize.getWidth() * 0.9, scrnsize.getHeight() * 0.9);
                                        dialog.setLocation(0, 0);
                                        dialog.setMaximumSize(scrnsize);
                                        dialog.setResizable(true);
                                        dialog.setVisible(true);
                                        System.out.println("PDFIP componenet count: " + pdfip.getComponentCount());
                                        ModifyPdf.PdfImagePdf(currentClient, pdfip, pdfTempFileName, newFileName);
                                    } else {
                                        PdfInstantPanel pdfip = new PdfInstantPanel(currentClient, pdfTempFileName);
                                        JOptionPane pane = new JOptionPane(pdfip, JOptionPane.YES_NO_CANCEL_OPTION);
                                        Icon icon = new ImageIcon();
                                        pane.setIcon(icon);
                                        JDialog dialog = pane.createDialog(null, pdfTempFileName);
                                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                                        Dimension scrnsize = toolkit.getScreenSize();
                                        scrnsize.setSize(scrnsize.getWidth() * 0.9, scrnsize.getHeight() * 0.9);
                                        dialog.setLocation(0, 0);
                                        dialog.setMaximumSize(scrnsize);
                                        dialog.setResizable(true);
                                        dialog.setVisible(true);
                                        System.out.println("PDFIP component count: " + pdfip.getComponentCount());
                                        ModifyPdf.PdfImagePdf(pdfip.getRect(), pdfTempFileName, newFileName, currentClient);
                                    }
                                    partIndexTobeRemoved.add(mbp);
                                } else {
                                    String otherFileName = mbp.getFileName();
                                    String otherExt = getExtension(otherFileName);
                                    if (otherExt == null) {
                                        otherExt = "";
                                    }
                                    mbp.saveFile(newFileName);
                                    partIndexTobeRemoved.add(mbp);
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        if (Global.DEBUG) System.out.println(" Array Index Out of Bounds");
                    }
                }
                if (Global.DEBUG) {
                    System.out.println("Writing modified message to :" + modifiedFolder + "EML" + File.separator + emlCodeNum + ".eml");
                }
                message.setFrom(new InternetAddress("cbbp@boshifilter.com"));
                message.removeHeader("Cc");
                message.setSubject(emlCodeNum + ": " + currentClient.getID() + " (" + message.getSubject() + ")");
                message.saveChanges();
                if (attachment) {
                    message.writeTo(new FileOutputStream(new File(modifiedFolder + "EML" + File.separator + emlCodeNum + "-m_A.eml")));
                } else {
                    message.writeTo(new FileOutputStream(new File(modifiedFolder + "EML" + File.separator + emlCodeNum + "-m.eml")));
                }
                if (Global.DEBUG) System.out.println("Source closed");
                source.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String editExcelInCoding(String codeName) {
        String outPath = ((BoshiGui) BoshiGui.getComponent()).getSettingDialog().getCodingFolder();
        FileInputStream ifs = null;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        try {
            ifs = new FileInputStream(new File(outPath + File.separator + year + "-EmailIn.xls"));
            if (Global.DEBUG) {
                System.out.println("Opened : " + outPath + File.separator + year + "-EmailIn.xls");
            }
            Workbook wb = null;
            wb = WorkbookFactory.create(ifs);
            int sheetNumber = wb.getNumberOfSheets();
            Sheet currentSheet = wb.getSheetAt(0);
            if (Global.DEBUG) {
                System.out.println("Last Current Sheet for " + codeName + " :" + currentSheet.getSheetName());
            }
            Row currentRow = currentSheet.getRow(Integer.parseInt(codeName));
            if (currentRow == null) {
                currentRow = currentSheet.createRow(Integer.parseInt(codeName));
            } else if (currentRow.getCell(1) == null) {
            } else if (currentRow.getCell(1).getStringCellValue().equals("")) {
            } else {
                int result = JOptionPane.showConfirmDialog(BoshiGui.getComponent(), "It seems " + codeName + " has been coded.\n Do you want to overwrite this Eml code?", codeName + " duplicated", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                } else {
                    return "";
                }
            }
            Cell nextCell = currentRow.getCell(0);
            if (nextCell == null) {
                currentRow.createCell(0);
                nextCell = currentRow.getCell(0);
            }
            nextCell.setCellValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1));
            nextCell = currentRow.getCell(1);
            if (nextCell == null) {
                currentRow.createCell(1);
                nextCell = currentRow.getCell(1);
            }
            String subYear = ("" + Calendar.getInstance().get(Calendar.YEAR)).substring(2);
            if (Global.DEBUG) {
                System.out.println("Year: " + Calendar.getInstance().get(Calendar.YEAR));
                System.out.println("SubYear = " + subYear);
                System.out.println("CodeNum = " + currentSheet.getLastRowNum());
            }
            nextCell.setCellValue("EI" + subYear + codeName);
            nextCell = currentRow.getCell(3);
            if (nextCell == null) {
                currentRow.createCell(3);
                nextCell = currentRow.getCell(3);
            }
            if (currentClient != null) {
                String clientName = currentClient.getID();
                nextCell.setCellValue(clientName);
            }
            wb.write(new FileOutputStream(new File(outPath + File.separator + year + "-EmailIn.xls")));
            return "EI" + subYear + codeName;
        } catch (FileNotFoundException e) {
            System.out.println("Cannot Opened/Write : " + outPath + File.separator + year + "-EmailIn.xls");
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ifs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getExtension(String s) {
        String ext = null;
        if (s == null) return "";
        int i = s.lastIndexOf('.');
        if (i == 0) ext = ""; else if (i > 0 && i < s.length() - 1) ext = s.substring(i + 1).toLowerCase();
        return ext;
    }

    private static String getDomainAddress(String s) {
        String domain = null;
        int i = s.lastIndexOf('@');
        if (i > 0 && i < s.length() - 1) {
            domain = s.substring(i + 1).toLowerCase();
        }
        return domain;
    }

    public static void main(String args[]) throws Exception {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(fc);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            emlParser(fc.getSelectedFile(), null);
        }
    }
}
