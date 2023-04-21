package gps.garmin;

import gps.garmin.img.FileParser;
import gps.garmin.img.Parser;
import gps.garmin.img.structure.IMG;
import gps.garmin.img.structure.data.MapLevel;
import gps.garmin.img.structure.data.ObjectType;
import gps.garmin.img.structure.data.Point;
import gps.garmin.img.structure.data.Polygon;
import gps.garmin.img.structure.data.Polyline;
import gps.garmin.img.structure.data.Subdivision;
import gps.garmin.img.structure.data.Subfile;
import gps.garmin.img.structure.data.SubfileTRE;
import gps.garmin.img.structure.fat.FATBlock;
import gps.garmin.img.structure.Header;
import gps.garmin.img.structure.fat.PartitionTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

public class Test {

    public Test() {
    }

    public static void main(String[] args) throws Exception {
        String dir = "/usr/local/development/Garmin IMG/src/gps/garmin/test";
        File[] files = new File(dir).listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return (name.endsWith(".img") || name.endsWith(".IMG")) && name.length() < 20;
            }
        });
        for (File file : files) {
            Parser parser = new FileParser(file);
            parser.parse();
            IMG img = parser.getIMG();
            Header header = img.getImgHeader();
            PartitionTable partitionTable = header.getPartitionTable();
            System.out.println("IMG " + img.getName() + " - " + file.length());
            System.out.println("XOR'd " + header.isXorEd());
            System.out.println("XOR byte " + header.getXorByte());
            System.out.println("Update month " + header.getUpdateMonth());
            System.out.println("Update year " + header.getUpdateYear());
            System.out.println("Checksum " + header.getChecksum());
            System.out.println("Signature " + header.getSignature());
            System.out.println("Creation date " + header.getCreationDate());
            System.out.println("Map file identifier " + header.getMapFileIdentifier());
            System.out.println("Description " + header.getDescription() + "...");
            System.out.println("Exponent1 " + header.getBlockSizeExponent1());
            System.out.println("Exponent2 " + header.getBlockSizeExponent2());
            System.out.println("Boot " + partitionTable.getBoot());
            System.out.println("Start head " + partitionTable.getStartHead());
            System.out.println("Start sector " + partitionTable.getStartSector());
            System.out.println("Start cylinder " + partitionTable.getStartCylinder());
            System.out.println("System type " + partitionTable.getSystemType());
            System.out.println("End head " + partitionTable.getEndHead());
            System.out.println("End sector " + partitionTable.getEndSector());
            System.out.println("End cylinder " + partitionTable.getEndCylinder());
            System.out.println("Rel sectors " + partitionTable.getRelSectors());
            System.out.println("Number of sectors " + partitionTable.getNumberOfSectors());
            System.out.println("Block size " + header.getFatBlockSize());
            System.out.println("Header size " + header.getHeaderSize());
            System.out.println("First subfile offset " + header.getFirstSubfileOffset());
            System.out.println("Block sequence count " + header.getBlockSequence().size());
            System.out.println("Blocks total size " + header.getFatBlocksTotalSize());
            for (FATBlock fatBlock : header.getFatBlocks()) {
                System.out.println("  Block type " + fatBlock.getType());
                System.out.println("    Subfile name " + fatBlock.getSubfileName());
                System.out.println("    Subfile type " + fatBlock.getSubfileType());
                System.out.println("    Subfile part " + fatBlock.getSubfilePart());
                System.out.println("    Subfile size " + fatBlock.getSubfileSizeInBytes());
                System.out.println("    Subfile offset " + fatBlock.getSubfileOffset());
                System.out.println("    Block sequence count " + fatBlock.getBlockSequence().size());
            }
            for (Subfile subfile : img.getSubfiles()) {
                System.out.println("        Subfile type " + subfile.getCommonHeader().getType());
                System.out.println("          Headers length " + subfile.getCommonHeader().getHeadersLength());
                System.out.println("          Offset " + subfile.getOffset());
                switch(subfile.getCommonHeader().getType()) {
                    case TRE:
                        SubfileTRE treSubfile = (SubfileTRE) subfile;
                        System.out.println("          North boundary " + treSubfile.getHeader().getNorthBoundary());
                        System.out.println("          East boundary " + treSubfile.getHeader().getEastBoundary());
                        System.out.println("          South boundary " + treSubfile.getHeader().getSouthBoundary());
                        System.out.println("          West boundary " + treSubfile.getHeader().getWestBoundary());
                        System.out.println("          Map levels section offset " + treSubfile.getHeader().getMapLevelsSectionOffset());
                        System.out.println("          Map levels section size " + treSubfile.getHeader().getMapLevelsSectionSize());
                        System.out.println("          Subdivisions section offset " + treSubfile.getHeader().getSubdivisionsSectionOffset());
                        System.out.println("          Subdivisions section size " + treSubfile.getHeader().getSubdivisionsSectionSize());
                        System.out.println("          Copyright section offset " + treSubfile.getHeader().getCopyrightSectionOffset());
                        System.out.println("          Copyright section size " + treSubfile.getHeader().getCopyrightSectionSize());
                        System.out.println("          Copyright record size " + treSubfile.getHeader().getCopyrightRecordSize());
                        System.out.println("          Transparent map " + treSubfile.getHeader().isTransparentMap());
                        System.out.println("          Show street before street number " + treSubfile.getHeader().isShowStreetBeforeStreetNumber());
                        System.out.println("          Show zip before city " + treSubfile.getHeader().isShowZipBeforeCity());
                        System.out.println("          Polyline section offset " + treSubfile.getHeader().getPolylineOverviewSectionOffset());
                        System.out.println("          Polyline section length " + treSubfile.getHeader().getPolylineOverviewSectionLength());
                        System.out.println("          Polyline records size " + treSubfile.getHeader().getPolylineOverviewRecordsSize());
                        System.out.println("          Polygon section offset " + treSubfile.getHeader().getPolygonOverviewSectionOffset());
                        System.out.println("          Polygon section length " + treSubfile.getHeader().getPolygonOverviewSectionLength());
                        System.out.println("          Polygon records size " + treSubfile.getHeader().getPolygonOverviewRecordsSize());
                        System.out.println("          Point section offset " + treSubfile.getHeader().getPointOverviewSectionOffset());
                        System.out.println("          Point section length " + treSubfile.getHeader().getPointOverviewSectionLength());
                        System.out.println("          Point records size " + treSubfile.getHeader().getPointOverviewRecordsSize());
                        System.out.println("          Map id " + treSubfile.getHeader().getMapID());
                        System.out.println("          TRE7 section offset " + treSubfile.getHeader().getTre7SectionOffset());
                        System.out.println("          TRE7 section length " + treSubfile.getHeader().getTre7SectionLength());
                        System.out.println("          TRE7 records size " + treSubfile.getHeader().getTre7RecordsSize());
                        System.out.println("          TRE8 section offset " + treSubfile.getHeader().getTre8SectionOffset());
                        System.out.println("          TRE8 section length " + treSubfile.getHeader().getTre8SectionLength());
                        System.out.println("          Map level encryption key " + treSubfile.getHeader().getMapLevelsSectionEncryptionKey());
                        System.out.println("          TRE9 section offset " + treSubfile.getHeader().getTre9SectionOffset());
                        System.out.println("          TRE9 section length " + treSubfile.getHeader().getTre9SectionLength());
                        System.out.println("          TRE9 records size " + treSubfile.getHeader().getTre9RecordsSize());
                        for (int copyright : treSubfile.getCopyrights()) {
                            System.out.println("              Copyright " + copyright);
                        }
                        for (MapLevel level : treSubfile.getMapLevels()) {
                            System.out.println("              Map level " + level.getNumber());
                            System.out.println("              Inherited " + level.isInherited());
                            System.out.println("              Zoom level " + level.getZoomLevel());
                            System.out.println("              Bits per coord " + level.getBitsPerCoordinate());
                            System.out.println("              Subdivisions " + level.getQuantityOfSubdivisions());
                        }
                        System.out.println("");
                        if (treSubfile.getFirstSubdivision() != null) {
                            showSubdivision(treSubfile.getFirstSubdivision());
                        }
                        System.out.println("");
                        for (Polyline polyline : treSubfile.getPolylines()) {
                            System.out.println("              Polyline number " + polyline.getNumber());
                            System.out.println("              Polyline type " + polyline.getType());
                            System.out.println("              Polyline maximum level " + polyline.getMaximumLevelWherePresent());
                        }
                        System.out.println("");
                        for (Polygon polygon : treSubfile.getPolygons()) {
                            System.out.println("              Polygon number " + polygon.getNumber());
                            System.out.println("              Polygon type " + polygon.getType());
                            System.out.println("              Polygon maximum level " + polygon.getMaximumLevelWherePresent());
                        }
                        System.out.println("");
                        for (Point point : treSubfile.getPoints()) {
                            System.out.println("              Point number " + point.getNumber());
                            System.out.println("              Point type " + point.getType());
                            System.out.println("              Point subtype " + point.getSubtype());
                            System.out.println("              Point maximum level " + point.getMaximumLevelWherePresent());
                        }
                        System.out.println("");
                        break;
                }
            }
            System.out.println("");
        }
    }

    private static void showSubdivision(Subdivision subdivision) {
        System.out.println("              Subdivision " + subdivision.getNumber() + " level " + subdivision.getLevel().getNumber());
        System.out.println("              RGN offset " + subdivision.getOffsetInRGNSubfile());
        for (ObjectType type : subdivision.getObjectTypes()) {
            System.out.println("                " + type);
        }
        System.out.println("              Longitude center " + subdivision.getLongitudeCenter());
        System.out.println("              Latitude center " + subdivision.getLatitudeCenter());
        System.out.println("              Width " + subdivision.getWidth());
        System.out.println("              Height " + subdivision.getHeight());
        System.out.println("              Terminating " + subdivision.isTerminatingFlag());
        System.out.println("              Total width " + subdivision.getTotalWidth());
        System.out.println("              Total height " + subdivision.getTotalHeight());
        if (subdivision.getContiguousSubdivision() != null) {
            System.out.println("--> Contiguous:");
            showSubdivision(subdivision.getContiguousSubdivision());
        }
        if (subdivision.getNextLevelSubdivision() != null) {
            System.out.println("--> Next level:");
            showSubdivision(subdivision.getNextLevelSubdivision());
        }
    }
}
