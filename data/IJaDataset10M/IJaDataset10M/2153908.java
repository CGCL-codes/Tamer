package nk.system.info;

import nk.records.RecordList;
import nk.records.RecordNameToken;

public class SystemProcessParser {

    private SystemCommandToken commandToken = null;

    private SystemInfoHandler handler = null;

    private RecordList recordList = null;

    private int tokenIterator = 0;

    private boolean started = false;

    public SystemProcessParser(SystemCommandToken commandToken, SystemInfoHandler handler) {
        this.commandToken = commandToken;
        this.handler = handler;
        recordList = new RecordList();
    }

    public void parse(String inputLine) {
        if (inputLine == null) {
            handler.handle(recordList);
        } else {
            switch(commandToken) {
                case HOST_NAME:
                    parseHostName(inputLine);
                    break;
                case SYSTEMINFO:
                    parseSystemInfo(inputLine.substring(27));
                    tokenIterator++;
                    break;
                case MOUNTVOL:
                    parseVolumeList(inputLine.substring(8));
                    break;
                case DIR:
                    getVolumeInfo(inputLine);
                    break;
                case TASKLIST:
                    getTaskList(inputLine);
                    break;
                case DRIVERQUERY:
                    getDriverList(inputLine);
                    break;
                case NETSTAT:
                    getNetstat(inputLine);
                    break;
                case IPCONFIG:
                    getIpconfig(inputLine);
                    break;
                case NETSTAT_E:
                    getNetstat_E(inputLine);
                    break;
            }
        }
    }

    private void parseHostName(String inputLine) {
        recordList.setRecord(RecordNameToken.HOST_NAME, inputLine.toUpperCase());
    }

    private void parseSystemInfo(String inputLine) {
        boolean oneLine = true;
        if (inputLine.compareTo("") != 0) {
            char ch = (char) inputLine.charAt(0);
            if (ch == '[' || ch == 32) {
                tokenIterator--;
                oneLine = false;
            }
        }
        switch(tokenIterator) {
            case 0:
                recordList.setRecord(RecordNameToken.HOST_NAME, inputLine);
                break;
            case 1:
                recordList.setRecord(RecordNameToken.OS_NAME, inputLine);
                break;
            case 2:
                recordList.setRecord(RecordNameToken.OS_VERSION, inputLine);
                break;
            case 3:
                recordList.setRecord(RecordNameToken.OS_MANUFACTURER, inputLine);
                break;
            case 4:
                recordList.setRecord(RecordNameToken.OS_CONFIGURATION, inputLine);
                break;
            case 5:
                recordList.setRecord(RecordNameToken.OS_BUILD_TYPE, inputLine);
                break;
            case 6:
                recordList.setRecord(RecordNameToken.REG_OWNER, inputLine);
                break;
            case 7:
                recordList.setRecord(RecordNameToken.REG_ORGANIZATION, inputLine);
                break;
            case 8:
                recordList.setRecord(RecordNameToken.PRODUCT_ID, inputLine);
                break;
            case 9:
                recordList.setRecord(RecordNameToken.ORIGINAL_INSTALL_DATE, inputLine);
                break;
            case 10:
                recordList.setRecord(RecordNameToken.SYS_UP_TIME, inputLine);
                break;
            case 11:
                recordList.setRecord(RecordNameToken.SYS_MANUFACTURER, inputLine);
                break;
            case 12:
                recordList.setRecord(RecordNameToken.SYS_MODEL, inputLine);
                break;
            case 13:
                recordList.setRecord(RecordNameToken.SYS_TYPE, inputLine);
                break;
            case 14:
                String inputProcesorList = null;
                if (oneLine == true) {
                    inputProcesorList = inputLine;
                } else {
                    inputProcesorList = recordList.getRecordValue(RecordNameToken.PROCESOR_LIST) + "\n" + inputLine;
                    oneLine = true;
                }
                recordList.setRecord(RecordNameToken.PROCESOR_LIST, inputProcesorList);
                break;
            case 15:
                recordList.setRecord(RecordNameToken.BIOS_VERSION, inputLine);
                break;
            case 16:
                recordList.setRecord(RecordNameToken.WIN_DIR, inputLine);
                break;
            case 17:
                recordList.setRecord(RecordNameToken.SYS_DIR, inputLine);
                break;
            case 18:
                recordList.setRecord(RecordNameToken.BOOT_DEVICE, inputLine);
                break;
            case 19:
                recordList.setRecord(RecordNameToken.SYS_LOCALE, inputLine);
                break;
            case 20:
                recordList.setRecord(RecordNameToken.INPUT_LOCALE, inputLine);
                break;
            case 21:
                recordList.setRecord(RecordNameToken.TIME_ZONE, inputLine);
                break;
            case 22:
                recordList.setRecord(RecordNameToken.TOTAL_PHYSICAL_MEMORY, inputLine);
                break;
            case 23:
                recordList.setRecord(RecordNameToken.AVAILABLE_PHYSICAL_MEMORY, inputLine);
                break;
            case 24:
                recordList.setRecord(RecordNameToken.VMEM_MAX_SIZE, inputLine);
                break;
            case 25:
                recordList.setRecord(RecordNameToken.VMEM_AVAILABLE, inputLine);
                break;
            case 26:
                recordList.setRecord(RecordNameToken.VMEM_IN_USE, inputLine);
                break;
            case 27:
                recordList.setRecord(RecordNameToken.PAGE_FILE_LOCATION, inputLine);
                break;
            case 28:
                recordList.setRecord(RecordNameToken.DOMAIN, inputLine);
                break;
            case 29:
                recordList.setRecord(RecordNameToken.LOGON_SERWER, inputLine);
                break;
            case 30:
                String inputHotfixes = null;
                if (oneLine == true) {
                    inputHotfixes = inputLine;
                } else {
                    oneLine = true;
                }
                recordList.setRecord(RecordNameToken.HOTFIX_COUNT, inputHotfixes);
                break;
            case 31:
                String inputNetworkList = null;
                if (oneLine == true) {
                    inputNetworkList = inputLine;
                } else {
                    inputNetworkList = recordList.getRecordValue(RecordNameToken.NETWORK_CARD_LIST) + "\n" + inputLine;
                    oneLine = true;
                }
                recordList.setRecord(RecordNameToken.NETWORK_CARD_LIST, inputNetworkList);
                break;
        }
    }

    private void parseVolumeList(String inputLine) {
        if (inputLine.contains("\\")) {
            String inputMountVol = recordList.getRecordValue(RecordNameToken.NETWORK_CARD_LIST);
            if (inputMountVol == null) {
                recordList.setRecord(RecordNameToken.NETWORK_CARD_LIST, inputLine);
            } else {
                recordList.setRecord(RecordNameToken.NETWORK_CARD_LIST, inputMountVol + "\n" + inputLine);
            }
        }
    }

    private void getVolumeInfo(String inputLine) {
        if (inputLine.contains("drive")) {
            recordList.setRecord(RecordNameToken.VOLUME_NAME, inputLine.substring(22));
        } else if (inputLine.contains("Serial")) {
            recordList.setRecord(RecordNameToken.VOLUME_SERIAL, inputLine.substring(25));
        } else if (inputLine.contains("Dir(")) {
            recordList.setRecord(RecordNameToken.VOLUME_SPACE_FREE, inputLine.substring(26));
        }
    }

    private void getTaskList(String inputLine) {
        if (started == true) {
            String inputTaskList = recordList.getRecordValue(RecordNameToken.TASK_LIST);
            if (inputTaskList == null) {
                recordList.setRecord(RecordNameToken.TASK_LIST, inputLine);
            } else {
                recordList.setRecord(RecordNameToken.TASK_LIST, inputTaskList + "\n" + inputLine);
            }
        }
        if (inputLine.contains("===")) {
            started = true;
        }
    }

    private void getDriverList(String inputLine) {
        if (started == true) {
            String inputDriverList = recordList.getRecordValue(RecordNameToken.DRIVER_LIST);
            if (inputDriverList == null) {
                recordList.setRecord(RecordNameToken.DRIVER_LIST, inputLine);
            } else {
                recordList.setRecord(RecordNameToken.DRIVER_LIST, inputDriverList + "\n" + inputLine);
            }
        }
        if (inputLine.contains("===")) {
            started = true;
        }
    }

    private void getNetstat(String inputLine) {
        if (started == true) {
            String inputNetstat = recordList.getRecordValue(RecordNameToken.NETSTAT);
            if (inputNetstat == null) {
                recordList.setRecord(RecordNameToken.NETSTAT, inputLine);
            } else {
                recordList.setRecord(RecordNameToken.NETSTAT, inputNetstat + "\n" + inputLine);
            }
        }
        if (inputLine.contains("Proto")) {
            started = true;
        }
    }

    private void getNetstat_E(String inputLine) {
        if (inputLine.contains("Bytes")) {
            started = true;
        }
        if (started == true) {
            String inputNetstat_E = recordList.getRecordValue(RecordNameToken.NETWORK_USAGE);
            if (inputNetstat_E == null) {
                recordList.setRecord(RecordNameToken.NETWORK_USAGE, inputLine);
            } else {
                recordList.setRecord(RecordNameToken.NETWORK_USAGE, inputNetstat_E + "\n" + inputLine);
            }
        }
    }

    private void getIpconfig(String inputLine) {
        if (inputLine.contains("Ethernet")) {
            started = true;
        }
        if (started == true) {
            String inputIP = recordList.getRecordValue(RecordNameToken.NETWORK_INTERFACE_LIST);
            if (inputIP == null) {
                recordList.setRecord(RecordNameToken.NETWORK_INTERFACE_LIST, inputLine);
            } else {
                recordList.setRecord(RecordNameToken.NETWORK_INTERFACE_LIST, inputIP + "\n" + inputLine);
            }
        }
    }
}
